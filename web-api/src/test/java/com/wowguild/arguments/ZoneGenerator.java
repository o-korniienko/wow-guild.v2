package com.wowguild.arguments;

import com.wowguild.dto.ZoneDto;
import com.wowguild.entity.rank.Zone;

public class ZoneGenerator {

    public static Zone generateZone(String name, long id, long canonicalId, String expansionName) {
        Zone zone = new Zone();
        zone.setId(id);
        zone.setZoneName(name);
        zone.setCanonicalId(canonicalId);
        zone.setExpansionName(expansionName);

        return zone;
    }

    public static ZoneDto generateZoneDto(String name, long id, long canonicalId, String expansionName) {
        ZoneDto zoneDto = new ZoneDto();
        zoneDto.setId(id);
        zoneDto.setZoneName(name);
        zoneDto.setCanonicalId(canonicalId);
        zoneDto.setExpansionName(expansionName);

        return zoneDto;
    }
}
