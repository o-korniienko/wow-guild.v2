package com.wowguild.converter;

import com.wowguild.dto.CharacterDto;
import com.wowguild.dto.CharacterRankDto;
import com.wowguild.dto.RankDto;
import com.wowguild.entity.Character;
import com.wowguild.entity.rank.CharacterRank;
import com.wowguild.entity.rank.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CharacterConverter implements Converter<Character, CharacterDto> {

    private final BossConverter bossConverter;

    @Override
    public Character convertToEntity(CharacterDto characterDto) {
        return null;
    }

    @Override
    public CharacterDto convertToDto(Character character) {
        if (character != null) {
            CharacterDto characterDto = new CharacterDto();
            characterDto.setName(character.getName());
            characterDto.setId(character.getId());
            characterDto.setRace(character.getRace());
            characterDto.setLevel(character.getLevel());
            characterDto.setRank(character.getRank());
            characterDto.setBlizzardID(character.getBlizzardID());
            characterDto.setCanonicalID(character.getCanonicalID());
            characterDto.setClassEn(character.getClassEn());
            characterDto.setRegionEn(character.getRegionEn());
            characterDto.setIconURL(character.getIconURL());
            if (character.getRanks() != null) {
                characterDto.setRanks(character.getRanks().stream()
                        .map(this::convertToCharacterRankDto).collect(Collectors.toList()));
            }
            return characterDto;
        }
        return null;
    }

    private CharacterRankDto convertToCharacterRankDto(CharacterRank characterRank) {
        if (characterRank != null) {
            CharacterRankDto characterRankDto = new CharacterRankDto();
            characterRankDto.setId(characterRank.getId());
            characterRankDto.setMetric(characterRank.getMetric());
            characterRankDto.setAverage(characterRank.getAverage());
            characterRankDto.setMaxAmount(characterRank.getMaxAmount());
            characterRankDto.setTotalKills(characterRank.getTotalKills());
            characterRankDto.setBoss(bossConverter.convertToDto(characterRank.getBoss()));
            if (characterRank.getRanks() != null) {
                characterRankDto.setRanks(characterRank.getRanks().stream()
                        .map(this::convertToRankDto).collect(Collectors.toList()));
            }
            return characterRankDto;
        }
        return null;
    }

    private RankDto convertToRankDto(Rank rank) {
        if (rank != null) {
            RankDto rankDto = new RankDto();
            rankDto.setId(rank.getId());
            rankDto.setMetric(rank.getMetric());
            rankDto.setAmount(rank.getAmount());
            rankDto.setFightID(rank.getFightID());
            rankDto.setKillIlvl(rank.getKillIlvl());
            rankDto.setDate(rank.getDate());
            rankDto.setReportCode(rank.getReportCode());
            return rankDto;
        }
        return null;
    }

}