package com.wowguild.service.guild;

import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.entity.rank.Report;
import com.wowguild.entity.rank.Zone;
import com.wowguild.model.wow_logs_models.WOWLogsFightData;
import com.wowguild.model.wow_logs_models.WOWLogsReportData;
import com.wowguild.sender.HttpSender;
import com.wowguild.service.entity.impl.CharacterService;
import com.wowguild.service.entity.impl.WowLogsReportService;
import com.wowguild.service.entity.impl.ZoneService;
import com.wowguild.service.token.TokenManager;
import com.wowguild.tool.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class WowLogsGuildService {

    private final Gson gson;


    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final WowLogsCharacterService wowLogsCharacterService;
    private final CharacterService characterService;
    private final WowLogsReportService reportService;
    private final ZoneService zoneService;
    private final JsonParser jsonParser;


    public WOWLogsReportData getReportData() {
        WOWLogsReportData report = new WOWLogsReportData();
        String token = tokenManager.getTokenByTag("wow_logs");

        try {
            Map<String, String> body = new HashMap<>();
            body.put("query", "{reportData {reports(guildName:\"Tauren Milfs\", guildServerSlug:\"tarren-mill\", guildServerRegion:\"EU\") {data{code,endTime}}}}");
            String url = "https://www.warcraftlogs.com/api/v2/client";
            String response = httpSender.sendRequest(url, body, HttpMethod.POST, token);
            if (!response.isEmpty()) {
                if (response.contains("429 Too Many Requests")) {
                    return report;
                }
                String reportJson = jsonParser.parse(response,"reports");
                //String reportsData = response.split("\"reports\":")[1];
                //reportsData = reportsData.substring(0, reportsData.length() - 3);
                report = gson.fromJson(reportJson, WOWLogsReportData.class);
                if (report == null){
                    report = new WOWLogsReportData();
                }
            }
        } catch (JsonSyntaxException e) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - getReportData got error: " + e.getMessage());
        }

        return report;
    }

    public boolean updateReportData(WOWLogsReportData reportData) {
        List<Report> reports = reportData.getData();
        List<WOWLogsFightData> fightData = new ArrayList<>();
        WOWLogsFightData ftData = null;
        if (reports != null) {
            for (Report report : reports) {
                boolean isReportAlreadyInDb = isReportAlreadyInDB(report);
                if (!isReportAlreadyInDb) {
                    reportService.save(report);
                    ftData = getReportFightData(report.getCode());

                    if (ftData != null) {
                        fightData.add(ftData);
                    }

                }
            }
        } else {
            return false;
        }
        return parsesCharactersAndBosses(fightData);
    }

    private WOWLogsFightData getReportFightData(String code) {
        WOWLogsFightData fight = null;
        String token = tokenManager.getTokenByTag("wow_logs");
        Map<String, String> body = new HashMap<>();
        String requestString = "{reportData {report(code:\"" + code + "\" ){fights(killType:Encounters){kill,name, difficulty, encounterID, gameZone{name,id}}zone{name,expansion{name},brackets{type,bucket,min,max}}rankedCharacters{name, canonicalID, server{slug}}}}}";
        body.put("query", requestString);

        String url = "https://www.warcraftlogs.com/api/v2/client";

        String response = httpSender.sendRequest(url, body, HttpMethod.POST, token);

        try {
            if (!response.isEmpty()) {
                if (response.contains("429 Too Many Requests")) {
                    return fight;
                }
                String fightJson = jsonParser.parse(response, "report");
                //String reportsData = response.split("\"report\":")[1];
                //reportsData = reportsData.substring(0, reportsData.length() - 3);
                fight = gson.fromJson(fightJson, WOWLogsFightData.class);
            }
        } catch (JsonSyntaxException e) {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - getReportFightData got error: " + e.getMessage());
        }

        return fight;
    }

    private boolean parsesCharactersAndBosses(List<WOWLogsFightData> reportsData) {
        boolean isThereNoErrors = true;
        Set<Character> characters = new HashSet<>();
        Set<Boss> bosses = new HashSet<>();
        for (WOWLogsFightData reportData : reportsData) {
            if (reportData.getRankedCharacters() == null || reportData.getFights() == null || reportData.getZone() == null || reportData.getZone().getName().equalsIgnoreCase("Mythic+ Dungeons")) {
                continue;
            }
            List<WOWLogsFightData.RankedCharacter> rankedCharacters = reportData.getRankedCharacters();

            for (WOWLogsFightData.RankedCharacter rankedCharacter : rankedCharacters) {
                Character character = null;
                try {
                    List<Character> charactersFromDB = characterService.findByName(rankedCharacter.getName());
                    if (charactersFromDB != null) {
                        for (Character character1 : charactersFromDB) {
                            if (character1.getRegionEn().equalsIgnoreCase(rankedCharacter.getServer().getSlug())) {
                                character = character1;
                            }
                        }
                    } /*else {
                        if (charactersFromDB.size() == 1) {
                            character = charactersFromDB.get(0);
                        }
                    }*/
                } catch (Exception e) {
                    System.out.println(rankedCharacter.getName());
                    isThereNoErrors = false;
                }
                if (character != null) {
                    character.setCanonicalID(rankedCharacter.getCanonicalID());
                    characterService.save(character);
                } else {
                    character = characterService.findByCanonicalID(rankedCharacter.getCanonicalID());

                }

                try {
                    if (character != null) {
                        characters.add(character);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(character);
                    isThereNoErrors = false;
                }
            }

            List<WOWLogsFightData.Fight> fights = reportData.getFights();

            /*Zone zoneFromDB = zoneService.findByZoneName(reportData.getZone().getName().trim());

            if (zoneFromDB == null) {
                zoneFromDB = new Zone();
                zoneFromDB.setZoneName(reportData.getZone().getName().trim());
                zoneFromDB.setExpansionName(reportData.getZone().getExpansion().getName().trim());
                zoneFromDB.setMaxLevel(reportData.getZone().getBrackets().getMax());

                zoneService.save(zoneFromDB);
            }*/

            for (WOWLogsFightData.Fight fight : fights) {
                Zone zoneFromDB = zoneService.findByCanonicalId(fight.getGameZone().getId());
                if (zoneFromDB == null) {
                    zoneFromDB = new Zone();
                    zoneFromDB.setZoneName(fight.getGameZone().getName().trim());
                    zoneFromDB.setExpansionName(reportData.getZone().getExpansion().getName().trim());
                    zoneFromDB.setCanonicalId(fight.getGameZone().getId());

                    zoneService.save(zoneFromDB);
                }
                if (fight.isKill() && fight.getEncounterID() != 0 && reportData.getZone() != null) {
                    Boss boss = new Boss();
                    boss.setDifficulty(fight.getDifficulty());
                    boss.setName(fight.getName());
                    boss.setEncounterID(fight.getEncounterID());
                    boss.setZone(zoneFromDB);
                    bosses.add(boss);
                }
            }
        }
        Set<Boss> finalBossList = bosses;

        for (Boss boss : bosses) {
            finalBossList = checkIfAllDifficultyOfBossesAdded(boss, finalBossList);
        }

        if (characters != null && finalBossList != null) {
            if (isThereNoErrors) {
                isThereNoErrors = wowLogsCharacterService.updateCharacters(characters, finalBossList);
            } else {
                wowLogsCharacterService.updateCharacters(characters, finalBossList);
            }
        }
        return isThereNoErrors;
    }

    private boolean isReportAlreadyInDB(Report report) {
        boolean result = false;
        List<Report> reports = reportService.findAll();

        for (Report reportFromDB : reports) {
            if (reportFromDB.getCode().equalsIgnoreCase(report.getCode())) {
                result = true;
            }
        }
        return result;
    }

    private Set<Boss> checkIfAllDifficultyOfBossesAdded(Boss boss, Set<Boss> bosses) {
        Set<Boss> result = new HashSet<>();
        boolean is3DifficultyAdded = false;
        boolean is4DifficultyAdded = false;
        boolean is5DifficultyAdded = false;
        for (Boss boss1 : bosses) {
            if (boss1.getEncounterID() == boss.getEncounterID()) {

                if (boss1.getDifficulty() == 3) {
                    is3DifficultyAdded = true;
                }
                if (boss1.getDifficulty() == 4) {
                    is4DifficultyAdded = true;
                }
                if (boss1.getDifficulty() == 5) {
                    is5DifficultyAdded = true;
                }
            }
            result.add(boss1);
        }

        if (!is3DifficultyAdded && (boss.getDifficulty() == 4 || boss.getDifficulty() == 5)) {
            Boss boss3Difficulty = new Boss();
            boss3Difficulty.setName(boss.getName().trim());
            boss3Difficulty.setDifficulty(3);
            boss3Difficulty.setZone(boss.getZone());
            boss3Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss3Difficulty);
        }
        if (!is4DifficultyAdded && (boss.getDifficulty() == 3 || boss.getDifficulty() == 5)) {
            Boss boss4Difficulty = new Boss();
            boss4Difficulty.setName(boss.getName().trim());
            boss4Difficulty.setDifficulty(4);
            boss4Difficulty.setZone(boss.getZone());
            boss4Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss4Difficulty);
        }
        if (!is5DifficultyAdded && (boss.getDifficulty() == 4 || boss.getDifficulty() == 3)) {
            Boss boss5Difficulty = new Boss();
            boss5Difficulty.setName(boss.getName().trim());
            boss5Difficulty.setDifficulty(5);
            boss5Difficulty.setZone(boss.getZone());
            boss5Difficulty.setEncounterID(boss.getEncounterID());
            result.add(boss5Difficulty);
        }

        return result;
    }


}
