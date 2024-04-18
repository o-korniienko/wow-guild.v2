package com.wowguild.controller;

import com.wowguild.converter.BossConverter;
import com.wowguild.converter.CharacterConverter;
import com.wowguild.dto.BossDto;
import com.wowguild.dto.CharacterDto;
import com.wowguild.dto.ZoneDto;
import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.service.guild.GuildManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final GuildManager guildManager;
    private final CharacterConverter characterConverter;
    private final BossConverter bossConverter;

    @GetMapping("/get_members")
    public List<CharacterDto> getMembers() {
        return guildManager.getMembers().stream()
                .map(characterConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_members_bz")
    public List<CharacterDto> updateMembersFromBlizzardDB() {
        return guildManager.updateMembersFromBlizzardDB().stream()
                .map(characterConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_ranking")
    public Map<String, List<BossDto>> updateMembersRanks() {
        Map<String, List<Boss>> updatingResult = guildManager.UpdateRankingData();
        Map<String, List<BossDto>> result = new HashMap<>();
        for (Map.Entry<String, List<Boss>> entry : updatingResult.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream()
                    .map(bossConverter::convertToDto)
                    .collect(Collectors.toList()));
        }
        return result;
    }

    @GetMapping("/get_raids")
    public Set<ZoneDto> getRaids() {
        return guildManager.getRaids().stream()
                .map(bossConverter::convertToZoneDto)
                .collect(Collectors.toSet());
    }

    @GetMapping("/get_bosses")
    public List<BossDto> getBosses() {
        return guildManager.getBosses().stream()
                .map(bossConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/get_ranked_members")
    public List<CharacterDto> getRankedMembers() {
        return guildManager.getRankedMembers().stream()
                .map(characterConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/update_character_data/{id}")
    public Map<String, CharacterDto> updateCharacterData(@PathVariable(value = "id") Character character) {
        Map<String, Character> updatingResult = guildManager.updateCharacterData(character);
        Map<String, CharacterDto> result = new HashMap<>();
        for (Map.Entry<String, Character> entry : updatingResult.entrySet()) {
            result.put(entry.getKey(), characterConverter.convertToDto(entry.getValue()));
        }
        return result;
    }

    @PostMapping("make_test")
    public List<String> makeTest() {
        return new ArrayList<>(Collections.singletonList("Success"));
    }
}
