package com.wowguild.common.model.rank;

import com.wowguild.common.dto.wow.RankDto;
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

}
