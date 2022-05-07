package com.after_dark.controller;

import com.after_dark.model.rank.Boss;
import com.after_dark.model.rank.Zone;
import com.after_dark.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.after_dark.model.character.Character;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class MemberController {

    @Autowired
    private CharacterService service;

    @GetMapping("/get_members")
    public List<Character> getMembers(){
        return service.getMembers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_members_bz")
    public List<Character> updateMembersFromBlizzardDB(){
        return service.updateMembersFromBlizzardDB();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update_ranking")
    public Map<String,List<Boss>> updateMembersRanks (){
        return service.UpdateRankingData();
    }

    @GetMapping("/get_raids")
    public Set<Zone> getRaids(){
        return service.getRaids();
    }

    @GetMapping("/get_bosses")
    public List<Boss> getBosses(){
        return service.getBosses();
    }

    @GetMapping("/get_ranked_members")
    public List<Character> getRankedMembers(){

        return service.getRankedMembers();
    }

    @PostMapping("/update_character_data/{id}")
    public Map<String,Character> updateCharacterData(@PathVariable (value = "id") Character character){
        return service.updateCharacterData(character);
    }
}
