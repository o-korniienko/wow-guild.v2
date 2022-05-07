package com.after_dark.repos;

import com.after_dark.model.InformingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfMessageRepos extends JpaRepository<InformingMessage,Long> {

    List<InformingMessage> findByTag(String tag);
}
