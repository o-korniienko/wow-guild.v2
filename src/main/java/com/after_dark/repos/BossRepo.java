package com.after_dark.repos;

import com.after_dark.model.rank.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BossRepo extends JpaRepository<Boss, Long> {
    List<Boss> findByEncounterID(long encounterID);
}
