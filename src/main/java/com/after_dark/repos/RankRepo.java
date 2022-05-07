package com.after_dark.repos;

import com.after_dark.model.rank.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepo extends JpaRepository<Rank,Long> {
}
