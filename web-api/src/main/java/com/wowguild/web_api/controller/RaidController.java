package com.wowguild.web_api.controller;

import com.wowguild.common.converter.BossConverter;
import com.wowguild.common.service.impl.BossService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RaidController {

    private final BossService bossService;
    private final BossConverter bossConverter;

    @GetMapping("/get_raids")
    public ResponseEntity<?> getBossesZones() {
        try {
            return ResponseEntity.ok(bossService.getBossesZones().stream()
                    .map(bossConverter::convertToZoneDto)
                    .collect(Collectors.toSet()));
        } catch (Exception e) {
            log.error("Could not get raids/zones. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get raids/zones.");
        }
    }

    @GetMapping("/get_bosses")
    public ResponseEntity<?> getBosses() {
        try {
            return ResponseEntity.ok(bossService.getAllSorted().stream()
                    .map(bossConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get bosses data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get bosses data.");
        }
    }
}
