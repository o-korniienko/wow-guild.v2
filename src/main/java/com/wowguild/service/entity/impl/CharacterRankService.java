package com.wowguild.service.entity.impl;

import com.wowguild.entity.rank.CharacterRank;
import com.wowguild.repos.CharacterRankRepo;
import com.wowguild.service.entity.EntityService;
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
    public List<CharacterRank> getAll() {
        return characterRankRepo.findAll();
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
