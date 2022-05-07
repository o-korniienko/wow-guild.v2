package com.after_dark.repos;

import com.after_dark.model.rank.Zone;
import com.after_dark.model.wow_logs_models.WOWLogsFightData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepo extends JpaRepository<Zone,Long> {

    Zone findByZoneName(String name);

}
