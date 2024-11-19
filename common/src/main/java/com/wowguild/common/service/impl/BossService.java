package com.wowguild.common.service.impl;

import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.Zone;
import com.wowguild.common.repos.wow.BossRepo;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BossService implements EntityService<Boss> {

    public static Comparator<Boss> BY_ENCOUNTER_ID = (o1, o2) -> (int) (o1.getEncounterID() - o2.getEncounterID());

    private final BossRepo bossRepo;

    @Override
    public void save(Boss boss) {
        bossRepo.save(boss);
    }

    public List<Boss> getAllSorted() {
        List<Boss> bosses = findAll();
        bosses = sort(bosses, BY_ENCOUNTER_ID, null);
        return bosses;
    }

    public Set<Zone> getBossesZones() {
        Set<Zone> result = new HashSet<>();
        List<Boss> bosses = findAll();
        for (Boss boss : bosses) {
            result.add(boss.getZone());
        }
        return result;
    }

    @Override
    public List<Boss> saveAll(List<Boss> bosses) {
        bossRepo.saveAll(bosses);
        return bosses;
    }

    @Override
    public List<Boss> findAll() {
        return bossRepo.findAll();
    }

    @Override
    public void delete(Boss boss) {
        bossRepo.delete(boss);
    }

    @Override
    public List<Boss> sort(List<Boss> bosses, Comparator<Boss> comparator1, Comparator<Boss> comparator2) {
        bosses.sort(comparator1);
        return bosses;
    }


    public List<Boss> findByEncounterID(long encounterID) {
        return bossRepo.findByEncounterID(encounterID);
    }
}
