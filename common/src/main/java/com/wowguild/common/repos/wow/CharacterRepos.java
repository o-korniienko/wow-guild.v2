package com.wowguild.common.repos.wow;

import com.wowguild.common.entity.wow.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepos extends JpaRepository<Character, Long> {

    List<Character> findByName(String name);

    Character findByCanonicalID(long canonicalID);

    @Query("SELECT c FROM Character c " +
            "JOIN c.ranks cr " +
            "JOIN cr.boss b " +
            "JOIN b.zone z " +
            "WHERE cr.metric = :metric " +
            "AND b.name = :bossName " +
            "AND b.difficulty = :difficulty " +
            "AND z.zoneName = :zoneName")
    List<Character> findRankedCharactersBy(
            @Param("metric") String metric,
            @Param("bossName") String bossName,
            @Param("difficulty") int difficulty,
            @Param("zoneName") String zoneName);
}
