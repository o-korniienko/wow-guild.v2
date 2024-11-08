package com.wowguild.web_api.service.wow;

import com.google.gson.JsonSyntaxException;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.Zone;
import com.wowguild.common.model.wow_logs.WowLogsWorldData;
import com.wowguild.common.service.impl.BossService;
import com.wowguild.common.service.impl.ZoneService;
import com.wowguild.web_api.sender.HttpSender;
import com.wowguild.web_api.service.token.TokenManager;
import com.wowguild.web_api.tool.parser.WowLogsWorldDataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WowLogsWorldDataService {

    @Value("${wow.logs.api}")
    private String wowLogsApi;

    private final WowLogsWorldDataParser wowLogsWorldDataParser;
    private final TokenManager tokenManager;
    private final HttpSender httpSender;
    private final BossService bossService;
    private final ZoneService zoneService;

    public String updateRaidsDataFromWowLogs() {
        String token = tokenManager.getTokenByTag("wow_logs");
        try {
            String query = "{worldData {zones {name id, encounters {name, id} expansion{name, id}}}}";

            Map<String, String> body = new HashMap<>();
            body.put("query", query);

            String response = httpSender.sendRequest(wowLogsApi, body, HttpMethod.POST, token);

            if (!response.isEmpty()) {
                if (response.contains("429 Too Many Requests")) {
                    return "Could not update raids data - 429 Too Many Requests";
                }

                WowLogsWorldData worldData = wowLogsWorldDataParser.parseTo(response);

                worldData.setZones(filterZonesBy(worldData.getZones(), "Mythic+", false));

                updateRaidsData(worldData);

                return "Successful";
            }
        } catch (JsonSyntaxException e) {
            log.error("Could not update raids data, error {}", e.getMessage());
        }

        return "Could not update raids data";
    }

    private void updateRaidsData(WowLogsWorldData worldData) {
        List<WowLogsWorldData.Zone> zones = worldData.getZones();
        for (WowLogsWorldData.Zone zone : zones) {
            Zone zoneFromDb = zoneService.findByCanonicalId(zone.getId());
            if (zoneFromDb == null) {
                zoneFromDb = new Zone();
                zoneFromDb.setZoneName(zone.getName());
                zoneFromDb.setExpansionName(zone.getExpansion().getName());
                zoneFromDb.setCanonicalId(zone.getId());

                zoneService.save(zoneFromDb);
            }

            List<WowLogsWorldData.Encounter> encounters = zone.getEncounters();
            for (WowLogsWorldData.Encounter encounter : encounters) {
                List<Boss> bossesFromDb = bossService.findByEncounterID(encounter.getId());
                checkIfAllDifficultyOfBossesAdded(bossesFromDb, encounter, zoneFromDb);
            }
        }
    }

    private void checkIfAllDifficultyOfBossesAdded(List<Boss> bossesFromDb,
                                                   WowLogsWorldData.Encounter encounter, Zone zone) {
        boolean is3DifficultyAdded = false;
        boolean is4DifficultyAdded = false;
        boolean is5DifficultyAdded = false;

        for (Boss boss : bossesFromDb) {
            if (boss.getDifficulty() == 3) {
                is3DifficultyAdded = true;
            }
            if (boss.getDifficulty() == 4) {
                is4DifficultyAdded = true;
            }
            if (boss.getDifficulty() == 5) {
                is5DifficultyAdded = true;
            }
        }

        if (!is3DifficultyAdded) {
            Boss boss3Difficulty = new Boss();
            boss3Difficulty.setName(encounter.getName().trim());
            boss3Difficulty.setDifficulty(3);
            boss3Difficulty.setZone(zone);
            boss3Difficulty.setEncounterID(encounter.getId());

            bossService.save(boss3Difficulty);
        }
        if (!is4DifficultyAdded) {
            Boss boss4Difficulty = new Boss();
            boss4Difficulty.setName(encounter.getName().trim());
            boss4Difficulty.setDifficulty(4);
            boss4Difficulty.setZone(zone);
            boss4Difficulty.setEncounterID(encounter.getId());

            bossService.save(boss4Difficulty);
        }
        if (!is5DifficultyAdded) {
            Boss boss5Difficulty = new Boss();
            boss5Difficulty.setName(encounter.getName().trim());
            boss5Difficulty.setDifficulty(5);
            boss5Difficulty.setZone(zone);
            boss5Difficulty.setEncounterID(encounter.getId());

            bossService.save(boss5Difficulty);
        }
    }

    private List<WowLogsWorldData.Zone> filterZonesBy(List<WowLogsWorldData.Zone> zones, String prefix,
                                                      boolean containsPrefix) {
        List<WowLogsWorldData.Zone> result = new ArrayList<>();

        for (WowLogsWorldData.Zone zone : zones) {
            if (containsPrefix) {
                if (zone.getName().contains(prefix)) {
                    result.add(zone);
                }
            } else {
                if (!zone.getName().contains(prefix)) {
                    result.add(zone);
                }
            }
        }

        return result;
    }
}
