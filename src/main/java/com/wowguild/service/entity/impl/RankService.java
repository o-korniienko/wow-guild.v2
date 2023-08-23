package com.wowguild.service.entity.impl;

import com.wowguild.entity.rank.Rank;
import com.wowguild.repos.RankRepo;
import com.wowguild.service.entity.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankService implements EntityService<Rank> {

    private final RankRepo rankRepo;

    @Override
    public void save(Rank rank) {
        rankRepo.save(rank);
    }

    @Override
    public List<Rank> getAll() {
        return rankRepo.findAll();
    }

    @Override
    public List<Rank> saveAll(List<Rank> ranks) {
        return rankRepo.saveAll(ranks);
    }

    @Override
    public List<Rank> findAll() {
        return rankRepo.findAll();
    }

    @Override
    public void delete(Rank rank) {
        rankRepo.delete(rank);
    }

    @Override
    public List<Rank> sort(List<Rank> ranks, Comparator<Rank> comparator1, Comparator<Rank> comparator2) {
        return null;
    }
}
