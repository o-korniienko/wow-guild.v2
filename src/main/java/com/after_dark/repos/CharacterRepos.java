package com.after_dark.repos;

import com.after_dark.model.character.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepos extends JpaRepository<Character, Long> {

    List<Character> findByName(String name);

    Character findByCanonicalID(long canonicalID);
}
