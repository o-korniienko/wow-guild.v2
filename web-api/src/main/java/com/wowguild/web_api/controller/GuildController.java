package com.wowguild.web_api.controller;

import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.web_api.service.wow.GuildManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/guild")
public class GuildController {

    private final GuildManager guildManager;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_guild_reports")
    public ResponseEntity<?> updateGuildReportsData() {
        try {
            String result = guildManager.updateWowLogsReports();

            return ResponseEntity.ok(new ApiResponse<>(result, 200));
        } catch (Exception e) {
            log.error("Could not update guild members rank data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update guild members rank data.");
        }
    }
}
