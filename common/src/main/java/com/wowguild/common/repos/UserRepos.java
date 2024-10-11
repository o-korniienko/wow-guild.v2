package com.wowguild.common.repos;

import com.wowguild.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepos extends JpaRepository<User,Long> {

    User findByUsername(@Param("name") String name);
}
