package com.wowguild.entity.rank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity(name = "wow_logs_report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String code;
    private long endTime;

    public Report() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id && endTime == report.endTime && Objects.equals(code, report.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, endTime);
    }

    @Override
    public String toString() {
        return "report{" +
                "code='" + code + '\'' +
                "endTime='" + endTime + '\'' +
                '}';
    }
}
