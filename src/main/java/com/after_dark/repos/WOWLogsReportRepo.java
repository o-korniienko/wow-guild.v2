package com.after_dark.repos;

import com.after_dark.model.wow_logs_models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WOWLogsReportRepo extends JpaRepository<Report,Long> {

}
