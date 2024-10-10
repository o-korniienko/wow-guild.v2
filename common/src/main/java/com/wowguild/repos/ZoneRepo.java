package com.wowguild.repos;

import com.wowguild.entity.rank.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepo extends JpaRepository<Zone,Long> {

    Zone findByZoneName(String name);

    Zone findByCanonicalId(long id);
}
