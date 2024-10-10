package com.wowguild.common.service.entity.impl;

import com.wowguild.common.entity.Character;
import com.wowguild.common.repos.CharacterRepos;
import com.wowguild.common.service.entity.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacterService implements EntityService<Character> {

    public static Comparator<Character> BY_GUILD_RANK = Comparator.comparing(Character::getRank);
    public static Comparator<Character> BY_LEVEL = (o1, o2) -> o2.getLevel() - o1.getLevel();
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
}
