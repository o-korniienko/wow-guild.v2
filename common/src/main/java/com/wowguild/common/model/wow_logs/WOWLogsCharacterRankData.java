package com.wowguild.common.model.wow_logs;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class WOWLogsCharacterRankData {

    private CharacterRankings encounterRankings;

    @Data
    @ToString
    public static class CharacterRankings {
        private double bestAmount;
        private double medianPerformance;
        private double averagePerformance;
        private int totalKills;
        private int fastestKill;
        private int difficulty;
        private String metric;
        private int partition;
        private int zone;
        private List<Rank> ranks;

        @Data
        @ToString
        public static class Rank {
            private boolean lockedIn;
            private double rankPercent;
            private double historicalPercent;
            private double todayPercent;
            private int rankTotalParses;
            private int historicalTotalParses;
            private int todayTotalParses;
            private Guild guild;
            private Report report;
            private int duration;
            private long startTime;
            private double amount;
            private int bracketData;
            private String spec;
            private String bestSpec;
            private int classType;
            private int faction;

            @Data
            @ToString
            public static class Guild {
                private int id;
                private String name;
                private int faction;
            }

            @Data
            @ToString
            public static class Report {
                private String code;
                private long startTime;
                private int fightID;
            }
        }
    }
}
