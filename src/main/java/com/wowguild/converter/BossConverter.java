package com.wowguild.converter;

import com.wowguild.dto.BossDto;
import com.wowguild.dto.ZoneDto;
import com.wowguild.entity.rank.Boss;
import com.wowguild.entity.rank.Zone;
import org.springframework.stereotype.Service;

@Service
public class BossConverter implements Converter<Boss, BossDto> {

    @Override
    public Boss convertToEntity(BossDto bossDto) {
        return null;
    }

    @Override
    public BossDto convertToDto(Boss boss) {
        if (boss != null) {
            BossDto bossDto = new BossDto();
            bossDto.setId(boss.getId());
            bossDto.setEncounterID(boss.getEncounterID());
            bossDto.setName(boss.getName());
            bossDto.setDifficulty(boss.getDifficulty());
            bossDto.setZone(convertToZoneDto(boss.getZone()));
            return bossDto;
        }
        return null;
    }

    public ZoneDto convertToZoneDto(Zone zone) {
        if (zone != null) {
            ZoneDto zoneDto = new ZoneDto();
            zoneDto.setId(zone.getId());
            zoneDto.setCanonicalId(zone.getCanonicalId());
            zoneDto.setZoneName(zone.getZoneName());
            zoneDto.setExpansionName(zone.getExpansionName());
            return zoneDto;
        }
        return null;
    }
}