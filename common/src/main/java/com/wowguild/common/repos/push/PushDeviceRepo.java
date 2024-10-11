package com.wowguild.common.repos.push;

import com.wowguild.common.entity.push.PushDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushDeviceRepo extends JpaRepository<PushDevice, Long> {

    PushDevice findByUserId(long id);
}
