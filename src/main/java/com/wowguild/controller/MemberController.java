package com.wowguild.controller;

import com.wowguild.converter.BossConverter;
import com.wowguild.converter.CharacterConverter;
import com.wowguild.dto.api.ApiResponse;
import com.wowguild.entity.Character;
import com.wowguild.service.entity.impl.BossService;
import com.wowguild.service.guild.GuildManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final GuildManager guildManager;
    private final CharacterConverter characterConverter;
    private final BossService bossService;
    private final BossConverter bossConverter;

    @GetMapping("/get_members")
    public ResponseEntity<?> getMembers() {
        try {
            return ResponseEntity.ok(guildManager.getMembers().stream()
                    .map(characterConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get guild members data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get guild members data.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_members_bz")
    public ResponseEntity<?> updateMembersFromBlizzardDB() {
        try {
            return ResponseEntity.ok(guildManager.updateMembersFromBlizzardDB().stream()
                    .map(characterConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not update guild members blizzard data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update guild members blizzard data.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_ranking")
    public ResponseEntity<?> updateMembersRanks() {
        try {
            String updatingResult = guildManager.UpdateRankingData();

            return ResponseEntity.ok(new ApiResponse<>(updatingResult, 200, bossService.getAllSorted().stream()
                    .map(bossConverter::convertToDto).collect(Collectors.toList())));
        } catch (Exception e) {
            log.error("Could not update guild members rank data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update guild members rank data.");
        }
    }


    @GetMapping("/get_ranked_members")
    public ResponseEntity<?> getRankedMembers() {
        try {
            return ResponseEntity.ok(guildManager.getRankedMembers().stream()
                    .map(characterConverter::convertToDto)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get ranked guild members. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get ranked guild members.");
        }
    }

    @PostMapping("/update_character_data/{id}")
    public ResponseEntity<?> updateCharacterData(@PathVariable(value = "id") long id) {
        try {
            Map<String, Character> updatingResult = guildManager.updateCharacterData(id);
            ApiResponse<Character> apiResponse = new ApiResponse<>();
            for (Map.Entry<String, Character> entry : updatingResult.entrySet()) {
                apiResponse.setMessage(entry.getKey());
                apiResponse.setData(entry.getValue());
                apiResponse.setStatus(200);
            }
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("Could not update character {} data. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Could not update character data");
        }
    }

    @PostMapping("make_test")
    public List<String> makeTest() {
        return new ArrayList<>(Collections.singletonList("Success"));
    }
}
