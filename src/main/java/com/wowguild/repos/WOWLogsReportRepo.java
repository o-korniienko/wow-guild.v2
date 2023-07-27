package com.wowguild.repos;

import com.wowguild.entity.rank.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WOWLogsReportRepo extends JpaRepository<Report,Long> {

}
