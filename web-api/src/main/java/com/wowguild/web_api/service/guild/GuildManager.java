package com.wowguild.web_api.service.guild;

import com.wowguild.common.entity.Character;
import com.wowguild.common.dto.UpdateStatus;
import com.wowguild.common.model.blizzard.GuildProfile;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import com.wowguild.common.service.entity.impl.CharacterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GuildManager {

    private final BattleNetGuildService battleNetGuildService;
    private final WowLogsGuildService wowLogsGuildService;
    private final BattleNetCharacterService battleNetCharacterService;
    private final WowLogsCharacterService wowLogsCharacterService;
    private final CharacterService characterService;


    public List<Character> updateMembersFromBlizzardDB() {
        List<Character> charactersFromBlizzardDB = battleNetGuildService.parseGuildData(battleNetGuildService.getGuildData());

        //characterRepos.deleteAll();
        List<Character> charactersFromOurDB = characterService.findAll();

        //charactersFromOurDB = addAllCharactersToDB(charactersFromBlizzardDB);

        if (charactersFromOurDB == null || charactersFromOurDB.isEmpty()) {
            charactersFromOurDB = characterService.saveAll(charactersFromBlizzardDB);
        } else {
            for (Character character : charactersFromOurDB) {
                if (!battleNetCharacterService.isContained(character, charactersFromBlizzardDB)) {
                    characterService.delete(character);
                }
            }

            for (Character character : charactersFromBlizzardDB) {
                if (!battleNetCharacterService.isContained(character, charactersFromOurDB)) {
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


    public String updateRankingData() {
        boolean isThereNoErrors = true;
        WOWLogsReportData reportData = wowLogsGuildService.getReportData();
        isThereNoErrors = wowLogsGuildService.updateReportData(reportData);
        if (isThereNoErrors) {
            return "Successful";
        } else {
            return "There were errors during updating rank data";
        }
    }

    public UpdateStatus<Character> updateCharacterData(long id) {
        Character character = characterService.findById(id);
        if (character != null) {
            GuildProfile guildProfile = battleNetGuildService.getGuildData();
            UpdateStatus<Character> updateCharacterStatus = battleNetCharacterService.updateCharacter(character, guildProfile);

            if (updateCharacterStatus.getResult() != null) {
                character = updateCharacterStatus.getResult();
            }
            return wowLogsCharacterService.updateCharacter(character, updateCharacterStatus.getStatus());
        }
        throw new EntityNotFoundException();
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
        return characterService.getAllSorted();
    }
}