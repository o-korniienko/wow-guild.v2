package com.wowguild.model.wow_logs_models;

import com.wowguild.entity.rank.Report;

import java.util.List;

public class WOWLogsReportData {

    private List<Report> data;

    public WOWLogsReportData() {
    }

    public List<Report> getData() {
        return data;
    }

    public void setData(List<Report> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WOWLogsReportData{" +
                "data=" + data +
                '}';
    }



}









