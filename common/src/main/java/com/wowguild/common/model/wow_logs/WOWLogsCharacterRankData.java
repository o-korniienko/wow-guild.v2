package com.wowguild.common.model.wow_logs;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        private Double bestAmount;
        private Double medianPerformance;
        private Double averagePerformance;
        private Integer totalKills;
        private Integer fastestKill;
        private Integer difficulty;
        private String metric;
        private Integer partition;
        private Integer zone;
        private List<Rank> ranks;

        @Data
        @ToString
        public static class Rank {
            private boolean lockedIn;
            private Double rankPercent;
            private Double historicalPercent;
            private Double todayPercent;
            private Integer rankTotalParses;
            private Integer historicalTotalParses;
            private Integer todayTotalParses;
            private Guild guild;
            private Report report;
            private Integer duration;
            private Long startTime;
            private Double amount;
            private Integer bracketData;
            private String spec;
            private String bestSpec;
            @JsonProperty("class")
            private Integer classType;
            private Integer faction;

            @Data
            @ToString
            public static class Guild {
                private Integer id;
                private String name;
                private Integer faction;
            }

            @Data
            @ToString
            public static class Report {
                private String code;
                private Long startTime;
                private Integer fightID;
            }
        }
    }
}
