package com.wowguild.common.service.impl;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.model.rank.RankedCharacter;
import com.wowguild.common.model.rank.RankedMembersSearch;
import com.wowguild.common.repos.wow.CharacterRepos;
import com.wowguild.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacterService implements EntityService<Character> {

    public static Comparator<Character> BY_GUILD_RANK = Comparator.comparing(Character::getGuildRank);
    public static Comparator<Character> BY_LEVEL = (o1, o2) -> o2.getLevel() - o1.getLevel();
    public static Comparator<RankedCharacter> BY_MAX = (o1, o2) -> Double.compare(o2.getMax(), o1.getMax());

    private final CharacterRepos characterRepos;

    @Override
    public void save(Character character) {
        characterRepos.save(character);
    }

    @Override
    public List<Character> getAllSorted() {
        List<Character> characters = findAll();
        characters = sort(characters, BY_GUILD_RANK, BY_LEVEL);
        return characters;
    }

    @Override
    public List<Character> saveAll(List<Character> characters) {
        characterRepos.saveAll(characters);
        return characters;
    }

    @Override
    public List<Character> sort(List<Character> characters, Comparator<Character> comparator1, Comparator<Character> comparator2) {
        characters.sort(comparator1.thenComparing(comparator2));
        return characters;
    }

    @Override
    public List<Character> findAll() {
        return characterRepos.findAll();
    }

    @Override
    public void delete(Character character) {
        characterRepos.delete(character);
    }

    public List<Character> findByName(String name) {
        return characterRepos.findByName(name);
    }

    public Character findByCanonicalID(long canonicalID) {
        return characterRepos.findByCanonicalID(canonicalID);
    }

    public Character findById(long id) {
        return characterRepos.findById(id).orElse(null);
    }

    public List<Character> getRankedMembersBy(RankedMembersSearch rankedMembersSearch) {
        return characterRepos.findRankedCharactersBy(rankedMembersSearch.getMetric(), rankedMembersSearch.getBossName(),
                rankedMembersSearch.getDifficulty(), rankedMembersSearch.getZoneName());
    }
}
