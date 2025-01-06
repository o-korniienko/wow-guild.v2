package com.wowguild.common.model.wow_logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class WowLogsWorldData {

    private List<Zone> zones;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Zone {

        private long id;
        private String name;
        private List<Encounter> encounters;
        private Expansion expansion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Encounter{
        private String name;
        private long id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Expansion{
        private String name;
        private long id;
    }
}
