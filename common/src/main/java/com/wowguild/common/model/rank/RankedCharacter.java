package com.wowguild.common.model.rank;

import com.wowguild.common.dto.wow.RankDto;
import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.entity.wow.rank.CharacterRank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class RankedCharacter {

    private int key;
    private long id;
    private String name;
    private String classEn;
    private double max;
    private double average;
    private int totalKills;
    private List<RankDto> ranks;

    //Creates an instance of RankedCharacter from Character instance and its Rank instance. Does not set ranks (List<RankDto>).
    public static RankedCharacter create(int key, Character character, CharacterRank characterRank) {
        return RankedCharacter.builder()
                .key(key)
                .id(character.getId())
                .name(character.getName())
                .classEn(character.getClassEn().name())
                .max(characterRank.getMaxAmount())
                .average(characterRank.getAverage())
                .totalKills(characterRank.getTotalKills())
                .build();
    }

}
