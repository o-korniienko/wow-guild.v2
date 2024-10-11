package com.wowguild.common.converter;

import com.wowguild.common.entity.wow.rank.Report;
import com.wowguild.common.model.wow_logs.WOWLogsReportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportConverter implements Converter<Report, WOWLogsReportData.ReportDto> {
    @Override
    public Report convertToEntity(WOWLogsReportData.ReportDto reportDto) {
        if (reportDto != null) {
            Report report = new Report();
            report.setCode(reportDto.getCode());
            report.setEndTime(reportDto.getEndTime());
            return report;
        }
        return null;
    }

    @Override
    public WOWLogsReportData.ReportDto convertToDto(Report report) {
        return null;
    }
}
