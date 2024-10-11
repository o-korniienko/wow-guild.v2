package com.wowguild.common.service.impl;

import com.wowguild.common.entity.push.PushDevice;
import com.wowguild.common.repos.push.PushDeviceRepo;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PushDeviceService implements EntityService<PushDevice> {

    private final PushDeviceRepo pushDeviceRepo;

    @Override
    public void save(PushDevice pushDevice) {
        pushDeviceRepo.save(pushDevice);
    }

    @Override
    public List<PushDevice> saveAll(List<PushDevice> entities) {
        return  pushDeviceRepo.saveAll(entities);
    }

    @Override
    public List<PushDevice> findAll() {
        return pushDeviceRepo.findAll();
    }

    public PushDevice findBiId(long id) {
        return pushDeviceRepo.findById(id).orElse(null);
    }

    public PushDevice findBiUserId(long id) {
        return pushDeviceRepo.findByUserId(id);
    }

    @Override
    public void delete(PushDevice pushDevice) {
        pushDeviceRepo.delete(pushDevice);
    }

    @Override
    public List<PushDevice> getAllSorted() {
        //TODO
        return List.of();
    }

    @Override
    public List<PushDevice> sort(List<PushDevice> entities, Comparator<PushDevice> comparator1, Comparator<PushDevice> comparator2) {
        //TODO
        return List.of();
    }
}
