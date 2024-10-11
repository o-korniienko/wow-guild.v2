package com.wowguild.common.repos.wow;

import com.wowguild.common.entity.wow.rank.CharacterRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRankRepo extends JpaRepository<CharacterRank,Long> {
}
