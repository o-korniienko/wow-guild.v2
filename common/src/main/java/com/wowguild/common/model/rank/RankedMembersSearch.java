package com.wowguild.common.model.rank;

import lombok.Data;

@Data
public class RankedMembersSearch {

    private String zoneName;
    private String bossName;
    private int difficulty;
    private String metric;
}
