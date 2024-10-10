package com.wowguild.model.wow_logs;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class WOWLogsFightData {
    private List<Fight> fights;
    private Zone zone;
    private List<RankedCharacter> rankedCharacters;

    @Data
    @ToString
    public static class Fight {
        private boolean kill;
        private String name;
        private int difficulty;
        private long encounterID;
        private FightZone gameZone;

        @Data
        @ToString
        public static class FightZone{
            private String name;
            private long id;

        }
    }

    @Data
    @ToString
    public static class Zone {

        private String name;
        private Expansion expansion;
        private Bracket brackets;

        @Data
        @ToString
        public static class Expansion{
            private String name;

        }

        @Data
        @ToString
        public static class Bracket{
            private String type;
            private int bucket;
            private int min;
            private int max;

        }
    }

    @Data
    @ToString
    public static class RankedCharacter {

        private String name;
        private long canonicalID;
        private Server server;

        @Data
        @ToString
        public static class Server{
            private String slug;

        }

    }

}