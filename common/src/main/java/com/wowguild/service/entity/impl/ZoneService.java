package com.wowguild.service.entity.impl;

import com.wowguild.entity.rank.Zone;
import com.wowguild.repos.ZoneRepo;
import com.wowguild.service.entity.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ZoneService implements EntityService<Zone> {

    private final ZoneRepo zoneRepo;

    @Override
    public void save(Zone zone) {
        zoneRepo.save(zone);
    }

    @Override
    public List<Zone> getAllSorted() {
        return findAll();
    }

    @Override
    public List<Zone> saveAll(List<Zone> zones) {
        return zoneRepo.saveAll(zones);
    }

    @Override
    public List<Zone> findAll() {
        return zoneRepo.findAll();
    }

    @Override
    public void delete(Zone zone) {
        zoneRepo.delete(zone);
    }

    @Override
    public List<Zone> sort(List<Zone> zones, Comparator<Zone> comparator1, Comparator<Zone> comparator2) {
        return null;
    }

    public Zone findByZoneName(String zoneName) {
        return zoneRepo.findByZoneName(zoneName);
    }

    public Zone findByCanonicalId(long id) {
        return zoneRepo.findByCanonicalId(id);
    }
}
