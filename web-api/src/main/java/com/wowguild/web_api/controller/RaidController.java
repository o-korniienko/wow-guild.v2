package com.wowguild.web_api.controller;

import com.wowguild.common.converter.RaidAndBossConverter;
import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.common.service.impl.ZoneService;
import com.wowguild.web_api.service.wow.WowLogsWorldDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/raid")
public class RaidController {

    private final WowLogsWorldDataService wowLogsWorldDataService;
    private final ZoneService zoneService;
    private final RaidAndBossConverter raidAndBossConverter;

    @GetMapping("/get-all")
    public ResponseEntity<?> getRaids() {
        try {
            return ResponseEntity.ok(zoneService.findAll().stream()
                    .map(raidAndBossConverter::convertToZoneDto)
                    .collect(Collectors.toSet()));
        } catch (Exception e) {
            log.error("Could not get raids/zones. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get raids/zones.");
        }
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update-wow-logs-data")
    public ResponseEntity<?> updateWowLogsRaidsData() {
        try {
            String resultMessage = wowLogsWorldDataService.updateRaidsDataFromWowLogs();

            return ResponseEntity.ok(new ApiResponse<>(resultMessage, 200));
        } catch (Exception e) {
            log.error("Could not get and update raids data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get and update raids data");
        }
    }

}
