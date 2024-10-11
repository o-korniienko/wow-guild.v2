package com.wowguild.common.repos;

import com.wowguild.common.entity.rank.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BossRepo extends JpaRepository<Boss, Long> {
    List<Boss> findByEncounterID(long encounterID);
}
