package com.wowguild.common.model.blizzard;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class CharacterProfile {


    private long id;
    private String name;
    private Race race;
    private CharacterClass character_class;
    private Realm realm;
    private int level;


    @Data
    public static class Race {
        private Map<String, String> key;
        private String name;
        private int id;

    }

    @Data
    public static class CharacterClass {
        private Map<String, String> key;
        private String name;
        private int id;

    }

    @Data
    public static class Realm {
        private Map<String, String> key;
        private String name;
        private int id;
        private String slug;

    }

}
