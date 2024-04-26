package com.wowguild.model.wow_logs;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class WOWLogsReportData {

    private List<ReportDto> data;

    @Data
    public static class ReportDto {
        private String code;
        private long endTime;

    }

}









