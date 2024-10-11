package com.wowguild.common.service.impl;

import com.wowguild.common.entity.wow.rank.CharacterRank;
import com.wowguild.common.repos.wow.CharacterRankRepo;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacterRankService implements EntityService<CharacterRank> {

    private final CharacterRankRepo characterRankRepo;

    @Override
    public void save(CharacterRank characterRank) {
        characterRankRepo.save(characterRank);
    }

    @Override
    public List<CharacterRank> getAllSorted() {
        return findAll();
    }

    @Override
    public List<CharacterRank> saveAll(List<CharacterRank> characterRanks) {
        return characterRankRepo.saveAll(characterRanks);
    }

    @Override
    public List<CharacterRank> findAll() {
        return characterRankRepo.findAll();
    }

    @Override
    public void delete(CharacterRank characterRank) {
        characterRankRepo.delete(characterRank);
    }

    @Override
    public List<CharacterRank> sort(List<CharacterRank> characterRanks, Comparator<CharacterRank> comparator1, Comparator<CharacterRank> comparator2) {
        characterRanks.sort(comparator1);
        return characterRanks;
    }
}
