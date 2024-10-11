package com.wowguild.web_api.controller;

import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.web_api.service.GuildEventScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GuildEventController {

    private final GuildEventScheduleService guildEventScheduleService;

    @PostMapping("/create-event")
    public ResponseEntity<?> scheduleEvent(@RequestBody GuildEventDto guildEventDto) {
        UpdateStatus<?> result = guildEventScheduleService.createEventSchedule(guildEventDto);
        if (result.getStatusCode() == 200){
            return ResponseEntity.ok("scheduled");
        }
        return ResponseEntity.badRequest().body(result.getStatus());
    }
}
