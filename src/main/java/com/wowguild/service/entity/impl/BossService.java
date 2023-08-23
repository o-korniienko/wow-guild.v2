package com.wowguild.service.entity.impl;

import com.wowguild.entity.rank.Boss;
import com.wowguild.repos.BossRepo;
import com.wowguild.service.entity.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BossService implements EntityService<Boss> {

    public static Comparator<Boss> BY_ENCOUNTER_ID = (o1, o2) -> (int) (o1.getEncounterID() - o2.getEncounterID());

    private final BossRepo bossRepo;

    @Override
    public void save(Boss boss) {
        bossRepo.save(boss);
    }

    @Override
    public List<Boss> getAll() {
        List<Boss> bosses = bossRepo.findAll();
        bosses = sort(bosses, BY_ENCOUNTER_ID, null);
        return bosses;
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
