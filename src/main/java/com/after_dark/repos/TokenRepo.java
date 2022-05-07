package com.after_dark.repos;


import com.after_dark.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, Long> {
     Token findByTag(String tag);
}
