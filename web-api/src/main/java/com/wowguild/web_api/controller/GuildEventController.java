package com.wowguild.web_api.controller;

import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.entity.security.User;
import com.wowguild.web_api.service.GuildEventScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Slf4j
public class GuildEventController {

    private final GuildEventScheduleService guildEventScheduleService;

    @PostMapping("/create")
    public ResponseEntity<?> scheduleEvent(@RequestBody GuildEventDto guildEventDto) {
        UpdateStatus<?> result = guildEventScheduleService.createEventSchedule(guildEventDto);
        if (result.getStatusCode() == 200) {
            return ResponseEntity.ok("scheduled");
        }
        return ResponseEntity.badRequest().body(result.getStatus());
    }

    @PostMapping("/subscribe/{id}")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal User user, @PathVariable("id") long id) {
        try {
            guildEventScheduleService.subscribe(user, id);
            return ResponseEntity.ok("Subscribed");
        } catch (EntityNotFoundException e) {
            log.error("Could not subscribe the user for event {}. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Could not subscribe the user for event {}. Error: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
