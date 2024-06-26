package com.wowguild.service.guild;

import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.entity.rank.CharacterRank;
import com.wowguild.entity.rank.Rank;
import com.wowguild.model.wow_logs_models.WOWLogsCharacterRankData;
import com.wowguild.sender.HttpSender;
import com.wowguild.service.entity.impl.BossService;
import com.wowguild.service.entity.impl.CharacterRankService;
import com.wowguild.service.entity.impl.CharacterService;
import com.wowguild.service.entity.impl.RankService;
import com.wowguild.service.token.TokenManager;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class WowLogsCharacterService {

    private final Gson gson;

    private final CharacterService characterService;
    private final BossService bossService;
    private final CharacterRankService characterRankService;
    private final RankService rankService;
    private final TokenManager tokenManager;
    private final HttpSender httpSender;

    public boolean updateCharacters(Set<Character> characters, Set<Boss> bosses) {
        boolean is429Error = false;
        boolean isThereNoErrors = true;
        for (int i = 0; i < 2; i++) {
            if (is429Error) {
                break;
            }
            String metric;
            if (i == 0) {
                metric = "dps";
            } else {
                metric = "hps";
            }
            for (Character character : characters) {
                if (is429Error) {
                    break;
                }
                if (character != null) {
                    List<CharacterRank> ranks = character.getRanks();
                    if (ranks == null) {
                        ranks = new ArrayList<>();
                    }

                    for (Boss boss : bosses) {
                        String response = "";
                        response = getCharacterData(boss, character, metric);
                        if (response == null) {
                            isThereNoErrors = false;
                        } else {
                            if (response.contains("429 Too Many Requests")) {
                                is429Error = true;
                                isThereNoErrors = false;
                                break;
                            }
                            ranks = parseCharacterWOWLogsData(response, character, boss, ranks);
                        }
                    }
                    character.setRanks(ranks);
                    characterService.save(character);
                }
            }
        }
        return isThereNoErrors;
    }

    public Map<String, Character> updateCharacter(Character character, String status) {
        Map<String, Character> result = new HashMap<>();
        List<Boss> bosses = bossService.findAll();
        boolean is429Error = false;
        boolean isThereNoErrors = true;
        if (bosses != null && bosses.size() > 0) {
            List<CharacterRank> ranks = character.getRanks();

            ranks = new ArrayList<>();
            if (ranks == null) {
            }
            String response = "";
            for (int i = 0; i < 2; i++) {
                if (is429Error) {
                    break;
                }
                String metric;
                if (i == 0) {
                    metric = "dps";
                } else {
                    metric = "hps";
                }
                for (Boss boss : bosses) {
                    response = getCharacterData(boss, character, metric);
                    if (response == null) {
                        isThereNoErrors = false;
                    } else {
                        if (response.contains("429 Too Many Requests")) {
                            is429Error = true;
                            isThereNoErrors = false;
                            break;
                        }
                        ranks = parseCharacterWOWLogsData(response, character, boss, ranks);
                    }
                }
            }
            character.setRanks(ranks);
        }
        if (status == null || status.equalsIgnoreCase("Successful")) {
            if (isThereNoErrors) {
                result.put("Successful", character);
            } else {
                result.put("there were errors during character data updating", character);
            }
        } else {
            result.put("there were errors during character data updating", character);
        }
        return result;
    }

    private List<CharacterRank> parseCharacterWOWLogsData(String response, Character character, Boss boss, List<CharacterRank> ranks) {
        WOWLogsCharacterRankData wowLogsRankData = null;
        if (response != null && !response.isEmpty()) {

            String result = null;
            try {
                result = response.split("\"encounterRankings\":")[1];
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(response);
            }

            String metric = null;

            result = result.substring(0, result.length() - 4);
            if (!result.contains("\"ranks\":[]")) {
                wowLogsRankData = gson.fromJson(result, WOWLogsCharacterRankData.class);
                if (wowLogsRankData != null) {
                    List<WOWLogsCharacterRankData.WOWLogsRank> wowLogsRanks = wowLogsRankData.getRanks();
                    List<Rank> fightRanks = new ArrayList<>();
                    try {
                        for (WOWLogsCharacterRankData.WOWLogsRank logsRank : wowLogsRanks) {

                            if (!isRankInOurDBAlready(logsRank, character, wowLogsRankData.getMetric())) {
                                Rank rank = new Rank();
                                rank.setDate(Instant.ofEpochMilli(logsRank.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                                rank.setAmount((long) logsRank.getAmount());
                                rank.setKillIlvl(logsRank.getBracketData());
                                rank.setFightID(String.valueOf(logsRank.getReport().getFightID()));
                                rank.setReportCode(logsRank.getReport().getCode());
                                rank.setMetric(wowLogsRankData.getMetric());
                                fightRanks.add(rank);
                            }
                        }

                        CharacterRank characterRank = null;
                        List<CharacterRank> currentCharacterRanks = character.getRanks();
                        if (currentCharacterRanks != null) {
                            for (CharacterRank rank : currentCharacterRanks) {
                                Boss boss1 = rank.getBoss();
                                if (boss1 != null) {
                                    if (boss1.getDifficulty() == boss.getDifficulty() && boss1.getName().equalsIgnoreCase(boss.getName()) && boss1.getEncounterID() == boss.getEncounterID() && rank.getMetric().equalsIgnoreCase(metric)) {
                                        characterRank = rank;
                                    }
                                }
                            }
                        }
                        if (characterRank == null) {
                            characterRank = new CharacterRank();
                            List<Boss> bossList = this.bossService.findByEncounterID(boss.getEncounterID());
                            Boss bossFromDB = null;
                            if (bossList != null && bossList.size() > 0) {
                                for (Boss boss1 : bossList) {
                                    if (boss1.getDifficulty() == boss.getDifficulty()) {
                                        bossFromDB = boss1;
                                    }
                                }
                            }
                            if (bossFromDB == null) {
                                this.bossService.save(boss);
                                characterRank.setBoss(boss);
                            } else {
                                characterRank.setBoss(bossFromDB);
                            }
                            characterRank.setRanks(fightRanks);
                            characterRank.setTotalKills(wowLogsRankData.getTotalKills());
                            characterRank.setMaxAmount((long) wowLogsRankData.getBestAmount());
                            characterRank.setAverage((long) getAverageDPS(fightRanks));
                            characterRank.setMetric(wowLogsRankData.getMetric());
                            rankService.saveAll(fightRanks);
                            characterRankService.save(characterRank);
                        } else {
                            List<Rank> ranksFromDB = characterRank.getRanks();
                            List<Rank> updatedRanks = new ArrayList<>();
                            if (ranksFromDB == null || ranksFromDB.size() == 0) {
                                updatedRanks = fightRanks;
                            } else {
                                updatedRanks = ranksFromDB;
                                for (Rank rank : fightRanks) {
                                    updatedRanks.add(rank);
                                }
                            }
                            characterRank.setRanks(updatedRanks);
                            characterRank.setMaxAmount((long) wowLogsRankData.getBestAmount());
                            characterRank.setAverage((long) getAverageDPS(updatedRanks));
                            characterRank.setMetric(wowLogsRankData.getMetric());
                            rankService.saveAll(updatedRanks);
                            characterRankService.save(characterRank);
                        }
                        ranks.add(characterRank);

                    } catch (Exception e) {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - parseCharacterWOWLogsData got error: " + e.getMessage());
                        System.out.println("wowLogsRankData: " + wowLogsRankData);
                        System.out.println("result: " + result);
                    }
                }
            }
        }
        return ranks;
    }

    private String getCharacterData(Boss boss, Character character, String metric) {
        String token = tokenManager.getTokenByTag("wow_logs");
        String result = "";

        if (token != null && !token.isEmpty()) {
            long encounterId = boss.getEncounterID();
            int difficulty = boss.getDifficulty();
            String characterName = character.getName();
            String server = character.getRegionEn();

            Map<String, String> body = new HashMap<>();
            String requestString = "{characterData{character(name:\"" + characterName + "\", serverSlug:\"" + server + "\", serverRegion:\"EU\"){encounterRankings(encounterID:" + encounterId + ", difficulty:" + difficulty + ", metric:" + metric + ")}}}";
            body.put("query", requestString);
            String url = "https://www.warcraftlogs.com/api/v2/client";
            try {
                result = httpSender.sendRequest(url, body, HttpMethod.POST, token);
                if (!result.isEmpty()) {

                    if (result.contains("Invalid difficulty\\/size specified") || result.contains("429 Too Many Requests")) {
                        System.out.println(requestString);
                        System.out.println(difficulty);

                        result = null;
                    }
                }
            } catch (RestClientException e) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - getCharacterData got error: " + e.getMessage());
                if (e.getMessage().contains("429")) {
                    result = e.getMessage();
                }
            }
        }
        return result;
    }

    private boolean isRankInOurDBAlready(WOWLogsCharacterRankData.WOWLogsRank logsRank, Character character, String metric) {
        boolean result = false;
        if (logsRank.getReport().getCode() == null) {
            return true;
        }
        List<CharacterRank> characterRankList = character.getRanks();
        if (characterRankList != null && characterRankList.size() > 0) {
            for (CharacterRank characterRank : characterRankList) {
                List<Rank> ranks = characterRank.getRanks();
                if (ranks != null) {
                    for (Rank rank : ranks) {
                        if (rank.getMetric().equalsIgnoreCase(metric) && rank.getReportCode().equalsIgnoreCase(logsRank.getReport().getCode()) && rank.getFightID().equalsIgnoreCase(String.valueOf(logsRank.getReport().getFightID()))) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    private double getAverageDPS(List<Rank> characterRanks) {
        double result = 0;

        for (Rank characterRank : characterRanks) {
            result = result + characterRank.getAmount();
        }

        result = result / characterRanks.size();

        return result;
    }
}
