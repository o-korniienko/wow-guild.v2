package com.wowguild.repos;

import com.wowguild.entity.rank.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepo extends JpaRepository<Rank,Long> {
}
