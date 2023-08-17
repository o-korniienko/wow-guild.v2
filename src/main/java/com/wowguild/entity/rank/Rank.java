package com.wowguild.entity.rank;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long amount;
    private int killIlvl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String reportCode;
    private String fightID;
    private String metric;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getKillIlvl() {
        return killIlvl;
    }

    public void setKillIlvl(int killIlvl) {
        this.killIlvl = killIlvl;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getFightID() {
        return fightID;
    }

    public void setFightID(String fightID) {
        this.fightID = fightID;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return Objects.equals(date, rank.date) && Objects.equals(reportCode, rank.reportCode) && Objects.equals(fightID, rank.fightID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, reportCode, fightID);
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", amount=" + amount +
                ", killIlvl=" + killIlvl +
                ", date=" + date +
                ", reportCode='" + reportCode + '\'' +
                ", fightID='" + fightID + '\'' +
                ", metric='" + metric + '\'' +
                '}';
    }
}
