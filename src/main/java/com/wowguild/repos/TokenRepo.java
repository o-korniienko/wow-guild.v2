package com.wowguild.repos;


import com.wowguild.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
     Token findByTag(String tag);
}
