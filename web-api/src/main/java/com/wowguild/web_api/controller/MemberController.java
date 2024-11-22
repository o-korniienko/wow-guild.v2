package com.wowguild.web_api.controller;

import com.wowguild.common.converter.CharacterConverter;
import com.wowguild.common.dto.api.ApiResponse;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.model.rank.RankedMembersSearch;
import com.wowguild.web_api.service.wow.GuildManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final GuildManager guildManager;
    private final CharacterConverter characterConverter;

    @GetMapping("/get-all")
    public ResponseEntity<?> getMembers() {
        try {
            return ResponseEntity.ok(guildManager.getMembers().stream()
                    .map(character -> characterConverter.convertPartlyToDto(character, false))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not get guild members data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get guild members data.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update-all-bz")
    public ResponseEntity<?> updateMembersFromBlizzardDB() {
        try {
            return ResponseEntity.ok(guildManager.updateMembersFromBlizzardDB().stream()
                    .map(character -> characterConverter.convertPartlyToDto(character, false))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Could not update guild members blizzard data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update guild members blizzard data.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update-all-rank")
    public ResponseEntity<?> updateMembersRanks() {
        try {
            String result = guildManager.updateRankingData();

            return ResponseEntity.ok(new ApiResponse<>(result, 200));
        } catch (Exception e) {
            log.error("Could not update guild members rank data. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not update guild members rank data.");
        }
    }

    @GetMapping("/get-all-ranked")
    public ResponseEntity<?> getRankedMembers() {
        try {
            return ResponseEntity.ok(guildManager.getAllRankedMembers());
        } catch (Exception e) {
            log.error("Could not get ranked guild members. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get ranked guild members.");
        }
    }

    @PostMapping("/get-all-ranked-by")
    public ResponseEntity<?> getRankedMembersBy(@RequestBody RankedMembersSearch rankedMembersSearch) {
        try {
            return ResponseEntity.ok(guildManager.getRankedMembersBy(rankedMembersSearch));
        } catch (Exception e) {
            log.error("Could not get ranked guild members. Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Could not get ranked guild members.");
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCharacterData(@PathVariable(value = "id") long id) {
        try {
            UpdateStatus<Character> updatingResult = guildManager.updateCharacterData(id);
            ApiResponse<Character> apiResponse = new ApiResponse<>();
            apiResponse.setData(updatingResult.getResult());
            apiResponse.setMessage(updatingResult.getStatus());
            apiResponse.setStatus(200);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("Could not update character {} data. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Could not update character data");
        }
    }

    @PostMapping("/make_test")
    public List<String> makeTest() {
        return new ArrayList<>(Collections.singletonList("Success"));
    }
}
