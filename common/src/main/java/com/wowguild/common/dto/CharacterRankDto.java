package com.wowguild.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class CharacterRankDto {

    private long id;
    private BossDto boss;
    private long maxAmount;
    private long average;
    private int totalKills;
    private String metric;
    private List<RankDto> ranks;
}
