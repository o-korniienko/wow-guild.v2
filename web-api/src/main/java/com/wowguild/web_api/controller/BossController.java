package com.wowguild.web_api.controller;

import com.wowguild.common.converter.RaidAndBossConverter;
import com.wowguild.common.service.impl.BossService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boss")
public class BossController {

    private final BossService bossService;
    private final RaidAndBossConverter raidAndBossConverter;

    @GetMapping("/get-all")
    public ResponseEntity<?> getBosses() {
        try {
            return ResponseEntity.ok(bossService.getAllSorted().stream()
                    .map(raidAndBossConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get bosses data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get bosses data.");
        }
    }
}
