package com.wowguild.common.service.impl;

import com.wowguild.common.entity.SimpleChatMessage;
import com.wowguild.common.repos.SimpleChatMessageRepo;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleChatMessageService implements EntityService<SimpleChatMessage> {

    private final SimpleChatMessageRepo simpleChatMessageRepo;

    @Override
    public void save(SimpleChatMessage entity) {
        simpleChatMessageRepo.save(entity);
    }

    public List<SimpleChatMessage> getAllSortedBy(Comparator<SimpleChatMessage> comparator) {
        List<SimpleChatMessage> messages = findAll();
        messages.sort(comparator);

        return messages;
    }

    @Override
    public List<SimpleChatMessage> saveAll(List<SimpleChatMessage> entities) {
        return simpleChatMessageRepo.saveAll(entities);
    }

    @Override
    public List<SimpleChatMessage> findAll() {
        return simpleChatMessageRepo.findAll();
    }

    @Override
    public void delete(SimpleChatMessage entity) {
        simpleChatMessageRepo.delete(entity);
    }

    @Override
    public List<SimpleChatMessage> sort(List<SimpleChatMessage> entities, Comparator<SimpleChatMessage> comparator1, Comparator<SimpleChatMessage> comparator2) {
        entities.sort(comparator1.thenComparing(comparator2));

        return entities;
    }
}
