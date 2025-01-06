package com.wowguild.common.repos.wow;

import com.wowguild.common.entity.wow.rank.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepo extends JpaRepository<Rank,Long> {
}
