package com.after_dark.repos;

import com.after_dark.model.rank.CharacterRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRankRepo extends JpaRepository<CharacterRank,Long> {
}
