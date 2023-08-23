package com.wowguild.controller;

import com.wowguild.entity.rank.Boss;
import com.wowguild.entity.rank.Zone;
import com.wowguild.service.guild.BattleNetCharacterService;
import com.wowguild.service.guild.GuildManager;
import com.wowguild.service.guild.BattleNetGuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.wowguild.entity.Character;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final BattleNetGuildService battleNetGuildService;
    private final GuildManager guildManager;
    private final BattleNetCharacterService characterService;

    @GetMapping("/get_members")
    public List<Character> getMembers(){
        return guildManager.getMembers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_members_bz")
    public List<Character> updateMembersFromBlizzardDB(){
        return guildManager.updateMembersFromBlizzardDB();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_ranking")
    public Map<String,List<Boss>> updateMembersRanks (){
        return guildManager.UpdateRankingData();
    }

    @GetMapping("/get_raids")
    public Set<Zone> getRaids(){
        return guildManager.getRaids();
    }

    @GetMapping("/get_bosses")
    public List<Boss> getBosses(){
        return guildManager.getBosses();
    }

    @GetMapping("/get_ranked_members")
    public List<Character> getRankedMembers(){
        return guildManager.getRankedMembers();
    }

    @PostMapping("/update_character_data/{id}")
    public Map<String,Character> updateCharacterData(@PathVariable (value = "id") Character character){
        return guildManager.updateCharacterData(character);
    }

    @PostMapping("make_test")
    public List<String> makeTest(){
        return new ArrayList<>(Collections.singletonList("Success"));
    }
}
