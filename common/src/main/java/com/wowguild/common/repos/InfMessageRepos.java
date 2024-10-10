package com.wowguild.common.repos;

import com.wowguild.common.entity.InformingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfMessageRepos extends JpaRepository<InformingMessage,Long> {

    List<InformingMessage> findByTag(String tag);
}
