package com.wowguild.common.service.impl;

import com.wowguild.common.entity.GuildEvent;
import com.wowguild.common.repos.GuildEventRepo;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuildEventService implements EntityService<GuildEvent> {

    private final GuildEventRepo guildEventRepo;

    @Override
    public void save(GuildEvent entity) {
        guildEventRepo.save(entity);
    }

    @Override
    public List<GuildEvent> saveAll(List<GuildEvent> entities) {
        return guildEventRepo.saveAll(entities);
    }

    @Override
    public List<GuildEvent> findAll() {
        return guildEventRepo.findAll();
    }

    @Override
    public void delete(GuildEvent entity) {
        guildEventRepo.delete(entity);
    }

    @Override
    public List<GuildEvent> sort(List<GuildEvent> entities, Comparator<GuildEvent> comparator1, Comparator<GuildEvent> comparator2) {
        return List.of();
    }

    public GuildEvent findById(long id) {
        return guildEventRepo.findById(id).orElse(null);
    }
}
