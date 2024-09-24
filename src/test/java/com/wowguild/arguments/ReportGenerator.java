package com.wowguild.arguments;

import com.wowguild.entity.rank.Report;
import com.wowguild.model.wow_logs.WOWLogsReportData;

public class ReportGenerator {

    public static Report generateReport(String code, long endTime) {
        Report report = new Report();
        report.setCode(code);
        report.setEndTime(endTime);

        return report;
    }

    public static WOWLogsReportData.ReportDto generateReportDto(String code, long endTime) {
        WOWLogsReportData.ReportDto report = new WOWLogsReportData.ReportDto();
        report.setCode(code);
        report.setEndTime(endTime);

        return report;
    }
}
