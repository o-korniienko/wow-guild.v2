package com.wowguild.service.guild;

import com.wowguild.entity.Character;
import com.wowguild.entity.rank.Boss;
import com.wowguild.entity.rank.Zone;
import com.wowguild.model.UpdateStatus;
import com.wowguild.model.wow_logs_models.WOWLogsReportData;
import com.wowguild.service.entity.impl.BossService;
import com.wowguild.service.entity.impl.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GuildManager {

    private final BattleNetGuildService battleNetGuildService;
    private final WowLogsGuildService wowLogsGuildService;
    private final BattleNetCharacterService battleNetCharacterService;
    private final WowLogsCharacterService wowLogsCharacterService;
    private final CharacterService characterService;
    private final BossService bossService;


    public List<Character> updateMembersFromBlizzardDB() {
        List<Character> charactersFromBlizzardDB = battleNetGuildService.parseGuildData(battleNetGuildService.getGuildData());

        //characterRepos.deleteAll();
        List<Character> charactersFromOurDB = characterService.findAll();

        //charactersFromOurDB = addAllCharactersToDB(charactersFromBlizzardDB);

        if (charactersFromOurDB == null || charactersFromOurDB.size() == 0) {
            charactersFromOurDB = characterService.saveAll(charactersFromBlizzardDB);
        } else {
            for (Character character : charactersFromOurDB) {
                if (!battleNetCharacterService.isContains(character, charactersFromBlizzardDB)) {
                    characterService.delete(character);
                }
            }

            for (Character character : charactersFromBlizzardDB) {
                if (!battleNetCharacterService.isContains(character, charactersFromOurDB)) {
                    characterService.save(character);
                }
            }
            charactersFromOurDB = characterService.findAll();
            for (Character our_character : charactersFromOurDB) {
                for (Character blizzard_character : charactersFromBlizzardDB) {
                    if (our_character.getBlizzardID().equalsIgnoreCase(blizzard_character.getBlizzardID())) {
                        our_character.setIconURL(blizzard_character.getIconURL());

                        our_character.setClassEn(blizzard_character.getClassEn());
                        our_character.setLevel(blizzard_character.getLevel());
                        our_character.setIconURL(blizzard_character.getIconURL());
                        our_character.setName(blizzard_character.getName());
                        our_character.setRace(blizzard_character.getRace());
                        our_character.setRank(blizzard_character.getRank());
                    }
                }
            }
        }
        charactersFromOurDB = characterService.sort(charactersFromOurDB, CharacterService.BY_GUILD_RANK, CharacterService.BY_LEVEL);
        return charactersFromOurDB;
    }


    public Map<String, List<Boss>> UpdateRankingData() {
        Map<String, List<Boss>> result = new HashMap<>();
        boolean isThereNoErrors = true;

        try {
            WOWLogsReportData reportData = wowLogsGuildService.getReportData();
            isThereNoErrors = wowLogsGuildService.updateReportData(reportData);
        } catch (RestClientException e) {
            e.printStackTrace();
            isThereNoErrors = false;
        }

        if (isThereNoErrors) {
            result.put("Successful", getBosses());
        } else {
            result.put("There were errors during updating rank data", getBosses());
        }
        return result;
    }

    public Map<String, Character> updateCharacterData(Character character) {
        Map<String, Character> result = new HashMap<>();

        String guildData = battleNetGuildService.getGuildData();
        UpdateStatus<Character> updateCharacterStatus = battleNetCharacterService.updateCharacter(character, guildData);

        if (updateCharacterStatus.getResult() != null) {
            character = updateCharacterStatus.getResult();
        }

        result = wowLogsCharacterService.updateCharacter(character, updateCharacterStatus.getStatus());
        return result;
    }

    public List<Boss> getBosses() {
        return bossService.getAll();
    }

    public Set<Zone> getRaids() {
        Set<Zone> result = new HashSet<>();
        List<Boss> bosses = bossService.findAll();
        for (Boss boss : bosses) {
            result.add(boss.getZone());
        }
        return result;
    }

    public List<Character> getRankedMembers() {
        List<Character> rankedCharacters = new ArrayList<>();
        List<Character> charactersFromDB = characterService.findAll();
        for (Character character : charactersFromDB) {

            if (character.getRanks() != null && !character.getRanks().isEmpty()) {
                rankedCharacters.add(character);
            }
        }
        return rankedCharacters;
    }

    public List<Character> getMembers() {
        return characterService.getAll();
    }
}
