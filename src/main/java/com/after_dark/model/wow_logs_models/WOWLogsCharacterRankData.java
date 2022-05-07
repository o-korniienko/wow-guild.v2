package com.after_dark.model.wow_logs_models;

import java.util.List;

public class WOWLogsCharacterRankData {

    private double bestAmount;
    private int totalKills;
    private String metric;
    private List<WOWLogsRank> ranks;


    public double getBestAmount() {
        return bestAmount;
    }

    public void setBestAmount(double bestAmount) {
        this.bestAmount = bestAmount;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public List<WOWLogsRank> getRanks() {
        return ranks;
    }

    public void setRanks(List<WOWLogsRank> ranks) {
        this.ranks = ranks;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    @Override
    public String toString() {
        return "WOWLogsCharacterRankData{" +
                "bestAmount=" + bestAmount +
                ", totalKills=" + totalKills +
                ", metric='" + metric + '\'' +
                ", ranks=" + ranks +
                '}';
    }

    public class WOWLogsRank {
        private long startTime;
        private double amount;
        private int bracketData;
        private Report report;


        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getBracketData() {
            return bracketData;
        }

        public void setBracketData(int bracketData) {
            this.bracketData = bracketData;
        }

        public Report getReport() {
            return report;
        }

        public void setReport(Report report) {
            this.report = report;
        }


        @Override
        public String toString() {
            return "WOWLogsRank{" +
                    "startTime=" + startTime +
                    ", amount=" + amount +
                    ", bracketData=" + bracketData +
                    ", report=" + report +
                    '}';
        }
    }

    public class Report {
        private String code;
        private long fightID;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public long getFightID() {
            return fightID;
        }

        public void setFightID(long fightID) {
            this.fightID = fightID;
        }

        @Override
        public String toString() {
            return "Report{" +
                    "code='" + code + '\'' +
                    ", fightID=" + fightID +
                    '}';
        }
    }
}
