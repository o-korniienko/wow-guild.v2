package com.wowguild.model.blizzard;

import lombok.Data;

import java.util.List;

@Data
public class GuildProfile {
    private Links _links;
    private Guild guild;
    private List<Member> members;

    @Data
    public static class Links {
        private Self self;

    }

    @Data
    public static class Self {
        private String href;

    }

    @Data
    public static class Guild {
        private Key key;
        private String name;
        private long id;
        private Realm realm;
        private Faction faction;

    }

    @Data
    public static class Key {
        private String href;

    }

    @Data
    public static class Realm {
        private Key key;
        private String name;
        private int id;
        private String slug;

    }

    @Data
    public static class Faction {
        private String type;
        private String name;

    }

    @Data
    public static class Member {
        private Character character;
        private int rank;

    }

    @Data
    public static class Character {
        private Key key;
        private String name;
        private long id;
        private Realm realm;
        private int level;
        private PlayableClass playable_class;
        private PlayableRace playable_race;

    }

    @Data
    public static class PlayableClass {
        private Key key;
        private int id;

    }

    @Data
    public static class PlayableRace {
        private Key key;
        private int id;

    }
}
