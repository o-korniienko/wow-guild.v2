package com.wowguild.common.repos;

import com.wowguild.common.entity.SimpleChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleChatMessageRepo extends JpaRepository<SimpleChatMessage, Long> {
}
