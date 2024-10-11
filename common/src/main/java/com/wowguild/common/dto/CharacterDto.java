package com.wowguild.common.dto;

import com.wowguild.common.enums.ClassEn;
import com.wowguild.common.enums.Rank;
import lombok.Data;

import java.util.List;

@Data
public class CharacterDto {

    private long id;
    private String name;
    private ClassEn classEn;
    private int level;
    private Rank rank;
    private String race;
    private String iconURL;
    private String regionEn;
    private String blizzardID;
    private long canonicalID;
    private List<CharacterRankDto> ranks;
}
