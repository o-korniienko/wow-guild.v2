package com.wowguild.dto;

import lombok.Data;

@Data
public class ZoneDto {
    private long id;

    private String zoneName;
    private String expansionName;
    private long canonicalId;
}
