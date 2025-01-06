package com.wowguild.common.dto.wow;

import lombok.Data;

@Data
public class BossDto {

    private long id;
    private String name;
    private long encounterID;
    private ZoneDto zone;
    private int difficulty;
}
