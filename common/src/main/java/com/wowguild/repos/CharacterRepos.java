package com.wowguild.repos;

import com.wowguild.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepos extends JpaRepository<Character, Long> {

    List<Character> findByName(String name);

    Character findByCanonicalID(long canonicalID);
}
