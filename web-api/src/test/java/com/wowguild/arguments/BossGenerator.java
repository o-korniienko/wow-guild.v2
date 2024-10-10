package com.wowguild.arguments;

import com.wowguild.common.dto.BossDto;
import com.wowguild.common.entity.rank.Boss;

public class BossGenerator {

    public static Boss generateBoss(String name, long id, int difficulty, int setEncounterID) {
        Boss boss = new Boss();
        boss.setName(name);
        boss.setId(id);
        boss.setDifficulty(difficulty);
        boss.setEncounterID(setEncounterID);
        boss.setZone(null);

        return boss;
    }

    public static BossDto generateBossDto(String name, long id, int difficulty, int setEncounterID) {
        BossDto bossDto = new BossDto();
        bossDto.setName(name);
        bossDto.setId(id);
        bossDto.setDifficulty(difficulty);
        bossDto.setEncounterID(setEncounterID);
        bossDto.setZone(null);

        return bossDto;
    }
}
