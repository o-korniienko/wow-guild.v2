package com.wowguild.arguments;

import com.wowguild.dto.CharacterDto;
import com.wowguild.dto.CharacterRankDto;
import com.wowguild.dto.RankDto;
import com.wowguild.entity.Character;
import com.wowguild.entity.rank.CharacterRank;
import com.wowguild.enums.ClassEn;
import com.wowguild.enums.Rank;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CharacterGenerator {

    public static Character generateCharacter(String name, LocalDateTime date) {
        Character character = new Character();
        character.setName(name);
        character.setId(1);
        character.setClassEn(ClassEn.Warrior);
        character.setLevel(80);
        character.setRank(Rank.Rank3);
        character.setRace("human");
        character.setIconURL("empty_url");
        character.setRegionEn("EU");
        character.setBlizzardID("123");
        character.setCanonicalID(1234567);
        List<CharacterRank> ranks = Collections.singletonList(generateCharacterRank(date));
        character.setRanks(ranks);

        return character;
    }

    public static CharacterRank generateCharacterRank(LocalDateTime date) {
        CharacterRank characterRank = new CharacterRank();
        characterRank.setId(1);
        characterRank.setBoss(null);
        characterRank.setMaxAmount(9999);
        characterRank.setAverage(5555);
        characterRank.setTotalKills(43);
        characterRank.setMetric("dps");
        List<com.wowguild.entity.rank.Rank> ranks = Collections.singletonList(generateRank(date));
        characterRank.setRanks(ranks);

        return characterRank;
    }

    public static com.wowguild.entity.rank.Rank generateRank(LocalDateTime date) {
        com.wowguild.entity.rank.Rank rank= new com.wowguild.entity.rank.Rank();
        rank.setId(1);
        rank.setAmount(4444);
        rank.setKillIlvl(608);
        rank.setDate(date);
        rank.setReportCode("12356");
        rank.setFightID("asd231");
        rank.setMetric("dps");

        return rank;
    }

    public static CharacterDto generateCharacterDto(String name, LocalDateTime date) {

        CharacterDto characterDto = new CharacterDto();
        characterDto.setName(name);
        characterDto.setId(1);
        characterDto.setClassEn(ClassEn.Warrior);
        characterDto.setLevel(80);
        characterDto.setRank(Rank.Rank3);
        characterDto.setRace("human");
        characterDto.setIconURL("empty_url");
        characterDto.setRegionEn("EU");
        characterDto.setBlizzardID("123");
        characterDto.setCanonicalID(1234567);
        List<CharacterRankDto> ranks = Collections.singletonList(generateCharacterRankDto(date));
        characterDto.setRanks(ranks);

        return characterDto;
    }

    public static CharacterRankDto generateCharacterRankDto(LocalDateTime date) {
        CharacterRankDto characterRankDto = new CharacterRankDto();
        characterRankDto.setId(1);
        characterRankDto.setBoss(null);
        characterRankDto.setMaxAmount(9999);
        characterRankDto.setAverage(5555);
        characterRankDto.setTotalKills(43);
        characterRankDto.setMetric("dps");
        List<RankDto> ranks = Collections.singletonList(generateRankDto(date));
        characterRankDto.setRanks(ranks);

        return characterRankDto;
    }

    public static RankDto generateRankDto(LocalDateTime date) {
        RankDto rankDto= new RankDto();
        rankDto.setId(1);
        rankDto.setAmount(4444);
        rankDto.setKillIlvl(608);
        rankDto.setDate(date);
        rankDto.setReportCode("12356");
        rankDto.setFightID("asd231");
        rankDto.setMetric("dps");

        return rankDto;
    }
}
