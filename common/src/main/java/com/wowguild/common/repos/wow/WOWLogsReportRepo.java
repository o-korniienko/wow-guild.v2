package com.wowguild.common.repos.wow;

import com.wowguild.common.entity.wow.rank.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WOWLogsReportRepo extends JpaRepository<Report,Long> {

    Report findByCode(String code);
}
