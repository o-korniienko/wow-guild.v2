package com.wowguild.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ZoneDto {
    private long id;

    private String zoneName;
    private String expansionName;
    private long canonicalId;
}
