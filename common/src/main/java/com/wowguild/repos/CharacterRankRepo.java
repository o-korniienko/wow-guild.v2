package com.wowguild.repos;

import com.wowguild.entity.rank.CharacterRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRankRepo extends JpaRepository<CharacterRank,Long> {
}
