package com.wowguild.web_api.service.guild;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.CharacterRank;
import com.wowguild.common.entity.wow.rank.Rank;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.model.wow_logs.WOWLogsCharacterRankData;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.common.service.impl.BossService;
import com.wowguild.common.service.impl.CharacterRankService;
import com.wowguild.common.service.impl.CharacterService;
import com.wowguild.common.service.impl.RankService;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowLogsCharacterService {

    @Value("${wow.logs.api}")
    private String wowLogsApi;

    private final CharacterService characterService;
    private final BossService bossService;
    private final CharacterRankService characterRankService;
    private final RankService rankService;
    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final Parser<WOWLogsCharacterRankData> characterRankDataParser;

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
                        if (response == null || response.isEmpty()) {
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

    public UpdateStatus<Character> updateCharacter(Character character, String status) {
        UpdateStatus<Character> result = new UpdateStatus<>();
        List<Boss> bosses = bossService.findAll();
        boolean is429Error = false;
        boolean isThereNoErrors = true;
        if (bosses != null && !bosses.isEmpty()) {
            List<CharacterRank> ranks = new ArrayList<>();

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
                    if (response == null || response.isEmpty()) {
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
                result.setStatus("Successful");
                result.setResult(character);
            } else {
                result.setStatus("there were errors during character data updating");
                result.setResult(character);
            }
        } else {
            result.setStatus("there were errors during character data updating");
            result.setResult(character);
        }
        return result;
    }

    private List<CharacterRank> parseCharacterWOWLogsData(String response, Character character, Boss boss, List<CharacterRank> ranks) {
        if (response != null && !response.isEmpty()) {
            WOWLogsCharacterRankData wowLogsRankData = characterRankDataParser.parseTo(response);
            String metric = wowLogsRankData.getEncounterRankings().getMetric();

            if (wowLogsRankData != null) {
                List<WOWLogsCharacterRankData.CharacterRankings.Rank> wowLogsRanks = wowLogsRankData
                        .getEncounterRankings().getRanks();
                List<Rank> fightRanks = new ArrayList<>();
                try {
                    for (WOWLogsCharacterRankData.CharacterRankings.Rank logsRank : wowLogsRanks) {

                        if (!isRankInOurDBAlready(logsRank, character,
                                wowLogsRankData.getEncounterRankings().getMetric())) {
                            Rank rank = new Rank();
                            rank.setDate(Instant.ofEpochMilli(
                                    logsRank.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                            rank.setAmount((long) logsRank.getAmount());
                            rank.setKillIlvl(logsRank.getBracketData());
                            rank.setFightID(String.valueOf(logsRank.getReport().getFightID()));
                            rank.setReportCode(logsRank.getReport().getCode());
                            rank.setMetric(wowLogsRankData.getEncounterRankings().getMetric());
                            fightRanks.add(rank);
                        }
                    }

                    CharacterRank characterRank = null;
                    List<CharacterRank> currentCharacterRanks = character.getRanks();
                    if (currentCharacterRanks != null) {
                        for (CharacterRank rank : currentCharacterRanks) {
                            Boss boss1 = rank.getBoss();
                            if (boss1 != null) {
                                if (boss1.getDifficulty() == boss.getDifficulty()
                                        && boss1.getName().equalsIgnoreCase(boss.getName())
                                        && boss1.getEncounterID() == boss.getEncounterID()
                                        && rank.getMetric().equalsIgnoreCase(metric)) {
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
                        characterRank.setTotalKills(wowLogsRankData.getEncounterRankings().getTotalKills());
                        characterRank.setMaxAmount((long) wowLogsRankData.getEncounterRankings().getBestAmount());
                        characterRank.setAverage((long) getAverageDPS(fightRanks));
                        characterRank.setMetric(wowLogsRankData.getEncounterRankings().getMetric());
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
                        characterRank.setMaxAmount((long) wowLogsRankData.getEncounterRankings().getBestAmount());
                        characterRank.setAverage((long) getAverageDPS(updatedRanks));
                        characterRank.setMetric(wowLogsRankData.getEncounterRankings().getMetric());
                        rankService.saveAll(updatedRanks);
                        characterRankService.save(characterRank);
                    }
                    ranks.add(characterRank);

                } catch (Exception e) {
                    log.error("Could not parse WOWLogs character data, error {}", e.getMessage());
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

            try {
                result = httpSender.sendRequest(wowLogsApi, body, HttpMethod.POST, token);
                if (!result.isEmpty()) {

                    if (result.contains("Invalid difficulty\\/size specified") || result.contains("429 Too Many Requests")) {
                        log.info("request string: {}", requestString);
                        log.info("difficulty: {}", difficulty);

                        result = null;
                    }
                }
            } catch (RestClientException e) {
                log.error("Could not get character data from WOWLogs, error {}", e.getMessage());
                if (e.getMessage().contains("429")) {
                    result = e.getMessage();
                }
            }
        }
        return result;
    }

    private boolean isRankInOurDBAlready(WOWLogsCharacterRankData.CharacterRankings.Rank logsRank, Character character, String metric) {
        boolean result = false;
        if (logsRank.getReport().getCode() == null) {
            return true;
        }
        List<CharacterRank> characterRankList = character.getRanks();
        if (characterRankList != null && !characterRankList.isEmpty()) {
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
