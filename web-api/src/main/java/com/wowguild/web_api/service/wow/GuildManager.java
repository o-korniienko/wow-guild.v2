package com.wowguild.web_api.service.wow;

import com.wowguild.common.converter.CharacterConverter;
import com.wowguild.common.dto.wow.UpdateStatus;
import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.CharacterRank;
import com.wowguild.common.model.blizzard.GuildProfile;
import com.wowguild.common.model.rank.RankedCharacter;
import com.wowguild.common.model.rank.RankedMembersSearch;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import com.wowguild.common.service.impl.CharacterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wowguild.common.service.impl.CharacterService.BY_MAX;

@RequiredArgsConstructor
@Service
public class GuildManager {

    private final BattleNetGuildService battleNetGuildService;
    private final WowLogsGuildService wowLogsGuildService;
    private final BattleNetCharacterService battleNetCharacterService;
    private final WowLogsCharacterService wowLogsCharacterService;
    private final CharacterService characterService;
    private final CharacterConverter characterConverter;
    private final WowLogsWorldDataService wowLogsWorldDataService;

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
                        our_character.setGuildRank(blizzard_character.getGuildRank());
                    }
                }
            }
        }
        charactersFromOurDB = characterService.sort(charactersFromOurDB, CharacterService.BY_GUILD_RANK, CharacterService.BY_LEVEL);
        return charactersFromOurDB;
    }


    public String updateWowLogsReports() {
        boolean isThereNoErrors = true;
        WOWLogsReportData reportData = wowLogsGuildService.getReports();
        isThereNoErrors = wowLogsGuildService.updateReports(reportData);
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

    public List<RankedCharacter> getRankedMembersBy(RankedMembersSearch rankedMembersSearch) {
        List<RankedCharacter> result = new ArrayList<>();
        List<Character> rankedCharacters = characterService.getRankedMembersBy(rankedMembersSearch);
        if (!rankedCharacters.isEmpty()) {
            int key = 0;
            for (Character character : rankedCharacters) {
                List<CharacterRank> characterRanks = character.getRanks();
                for (CharacterRank characterRank : characterRanks) {
                    if (characterRank.getMetric().equals(rankedMembersSearch.getMetric())
                            && characterRank.getBoss().getName().equals(rankedMembersSearch.getBossName())
                            && characterRank.getBoss().getDifficulty() == rankedMembersSearch.getDifficulty()
                            && characterRank.getBoss().getZone().getZoneName().equals(rankedMembersSearch.getZoneName())) {
                        RankedCharacter rankedCharacter = RankedCharacter.builder()
                                .key(key)
                                .id(character.getId())
                                .name(character.getName())
                                .classEn(character.getClassEn().name())
                                .max(characterRank.getMaxAmount())
                                .average(characterRank.getAverage())
                                .totalKills(characterRank.getTotalKills())
                                .ranks(characterRank.getRanks().stream()
                                        .map(characterConverter::convertToRankDto)
                                        .collect(Collectors.toList())
                                )
                                .build();
                        result.add(rankedCharacter);

                        key++;
                    }
                }
            }
        }

        result.sort(BY_MAX);
        return result;
    }

    public String updateRankingData() {
        Set<Boss> bosses = wowLogsWorldDataService.getRaidsData();
        if (bosses == null || bosses.isEmpty()) {
            return "Could not update guild members rank data";
        }
        return wowLogsCharacterService.updateCharacters(bosses) ? "Successful" :
                "There were errors during updating of guild members rank data";
    }
}