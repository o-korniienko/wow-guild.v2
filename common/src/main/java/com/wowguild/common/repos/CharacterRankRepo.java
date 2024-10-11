package com.wowguild.common.repos;

import com.wowguild.common.entity.rank.CharacterRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRankRepo extends JpaRepository<CharacterRank,Long> {
}
