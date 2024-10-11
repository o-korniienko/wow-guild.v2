package com.wowguild.common.repos;

import com.wowguild.common.entity.GuildEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildEventRepo extends JpaRepository<GuildEvent, Long> {
}
