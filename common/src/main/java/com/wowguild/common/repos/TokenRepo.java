package com.wowguild.common.repos;


import com.wowguild.common.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
     Token findByTag(String tag);
}
