package com.wowguild.arguments;

import com.wowguild.entity.Character;
import com.wowguild.model.blizzard.GuildProfile;

import java.util.ArrayList;
import java.util.List;

public class GuildAndCharacterGenerator {

    public static String getBattleNetGuildProfileJson() {
        return """
                {
                  "_links": {
                    "self": {
                      "href": "https://api.wowguild.com/guild-profile"
                    }
                  },
                  "guild": {
                    "key": {
                      "href": "https://api.wowguild.com/guild"
                    },
                    "name": "Raiders of the Lost Loot",
                    "id": 12345,
                    "realm": {
                      "key": {
                        "href": "https://api.wowguild.com/realm"
                      },
                      "name": "Stormrage",
                      "id": 567,
                      "slug": "stormrage"
                    },
                    "faction": {
                      "type": "ALLIANCE",
                      "name": "Alliance"
                    }
                  },
                  "members": [
                    {
                      "character": {
                        "key": {
                          "href": "https://api.wowguild.com/character/123"
                        },
                        "name": "Thaloran",
                        "id": 123,
                        "realm": {
                          "key": {
                            "href": "https://api.wowguild.com/realm"
                          },
                          "name": "Stormrage",
                          "id": 567,
                          "slug": "stormrage"
                        },
                        "level": 60,
                        "playable_class": {
                          "key": {
                            "href": "https://api.wowguild.com/class"
                          },
                          "id": 1
                        },
                        "playable_race": {
                          "key": {
                            "href": "https://api.wowguild.com/race"
                          },
                          "id": 5
                        }
                      },
                      "rank": 0
                    },
                    {
                      "character": {
                        "key": {
                          "href": "https://api.wowguild.com/character/124"
                        },
                        "name": "Valindra",
                        "id": 124,
                        "realm": {
                          "key": {
                            "href": "https://api.wowguild.com/realm"
                          },
                          "name": "Stormrage",
                          "id": 567,
                          "slug": "stormrage"
                        },
                        "level": 60,
                        "playable_class": {
                          "key": {
                            "href": "https://api.wowguild.com/class"
                          },
                          "id": 3
                        },
                        "playable_race": {
                          "key": {
                            "href": "https://api.wowguild.com/race"
                          },
                          "id": 8
                        }
                      },
                      "rank": 1
                    },
                    {
                      "character": {
                        "key": {
                          "href": "https://api.wowguild.com/character/125"
                        },
                        "name": "Zelara",
                        "id": 125,
                        "realm": {
                          "key": {
                            "href": "https://api.wowguild.com/realm"
                          },
                          "name": "Stormrage",
                          "id": 567,
                          "slug": "stormrage"
                        },
                        "level": 60,
                        "playable_class": {
                          "key": {
                            "href": "https://api.wowguild.com/class"
                          },
                          "id": 5
                        },
                        "playable_race": {
                          "key": {
                            "href": "https://api.wowguild.com/race"
                          },
                          "id": 27
                        }
                      },
                      "rank": 2
                    },
                    {
                      "character": {
                        "key": {
                          "href": "https://api.wowguild.com/character/126"
                        },
                        "name": "Gormak",
                        "id": 126,
                        "realm": {
                          "key": {
                            "href": "https://api.wowguild.com/realm"
                          },
                          "name": "Stormrage",
                          "id": 567,
                          "slug": "stormrage"
                        },
                        "level": 60,
                        "playable_class": {
                          "key": {
                            "href": "https://api.wowguild.com/class"
                          },
                          "id": 7
                        },
                        "playable_race": {
                          "key": {
                            "href": "https://api.wowguild.com/race"
                          },
                          "id": 28
                        }
                      },
                      "rank": 3
                    },
                    {
                      "character": {
                        "key": {
                          "href": "https://api.wowguild.com/character/127"
                        },
                        "name": "Fenthis",
                        "id": 127,
                        "realm": {
                          "key": {
                            "href": "https://api.wowguild.com/realm"
                          },
                          "name": "Stormrage",
                          "id": 567,
                          "slug": "stormrage"
                        },
                        "level": 60,
                        "playable_class": {
                          "key": {
                            "href": "https://api.wowguild.com/class"
                          },
                          "id": 9
                        },
                        "playable_race": {
                          "key": {
                            "href": "https://api.wowguild.com/race"
                          },
                          "id": 10
                        }
                      },
                      "rank": 4
                    }
                  ]
                }
                """;
    }

    public static GuildProfile getBattleNetGuildProfileObject(){
        // Create the Links
        GuildProfile.Links links = new GuildProfile.Links();
        GuildProfile.Self self = new GuildProfile.Self();
        self.setHref("https://api.wowguild.com/guild-profile");
        links.setSelf(self);

        // Create the Realm
        GuildProfile.Realm realm = new GuildProfile.Realm();
        GuildProfile.Key realmKey = new GuildProfile.Key();
        realmKey.setHref("https://api.wowguild.com/realm");
        realm.setKey(realmKey);
        realm.setName("Stormrage");
        realm.setId(567);
        realm.setSlug("stormrage");

        // Create the Faction
        GuildProfile.Faction faction = new GuildProfile.Faction();
        faction.setType("ALLIANCE");
        faction.setName("Alliance");

        // Create the Guild
        GuildProfile.Guild guild = new GuildProfile.Guild();
        GuildProfile.Key guildKey = new GuildProfile.Key();
        guildKey.setHref("https://api.wowguild.com/guild");
        guild.setKey(guildKey);
        guild.setName("Raiders of the Lost Loot");
        guild.setId(12345);
        guild.setRealm(realm);
        guild.setFaction(faction);

        // Create Members List
        List<GuildProfile.Member> members = new ArrayList<>();

        // Create Characters and Members
        members.add(createMember("Thaloran", 123, 60, 1, 5, 0));
        members.add(createMember("Valindra", 124, 60, 3, 8, 1));
        members.add(createMember("Zelara", 125, 60, 5, 27, 2));
        members.add(createMember("Gormak", 126, 60, 7, 28, 3));
        members.add(createMember("Fenthis", 127, 60, 9, 10, 4));

        // Create the GuildProfile
        GuildProfile guildProfile = new GuildProfile();
        guildProfile.set_links(links);
        guildProfile.setGuild(guild);
        guildProfile.setMembers(members);


        return guildProfile;
    }

    public static List<Character> getCharacterList() {
        List<Character> characterList = new ArrayList<>();

        Character character1 = new Character();
        character1.setId(123);
        character1.setName("Thaloran");
        character1.setClassEnByInt(1); // Warrior
        character1.setLevel(60);
        character1.setRankByInt(0); // Guild Master
        character1.setRaceByID(5); // Undead
        character1.setBlizzardID("123");
        character1.setCanonicalID(123);
        character1.setIconURL("https://api.wowguild.com/icon/123");
        character1.setRegionEn("NA");
        character1.setRanks(null);
        characterList.add(character1);

        Character character2 = new Character();
        character2.setId(124);
        character2.setName("Valindra");
        character2.setClassEnByInt(3); // Hunter
        character2.setLevel(60);
        character2.setRankByInt(1); // Rank 2
        character2.setRaceByID(8); // Troll
        character2.setBlizzardID("124");
        character2.setCanonicalID(124);
        character2.setIconURL("https://api.wowguild.com/icon/124");
        character2.setRegionEn("NA");
        character2.setRanks(null);
        characterList.add(character2);

        Character character3 = new Character();
        character3.setId(125);
        character3.setName("Zelara");
        character3.setClassEnByInt(5); // Priest
        character3.setLevel(60);
        character3.setRankByInt(2); // Rank 3
        character3.setRaceByID(27); // Nightborne
        character3.setBlizzardID("125");
        character3.setCanonicalID(125);
        character3.setIconURL("https://api.wowguild.com/icon/125");
        character3.setRegionEn("NA");
        character3.setRanks(null);
        characterList.add(character3);

        Character character4 = new Character();
        character4.setId(126);
        character4.setName("Gormak");
        character4.setClassEnByInt(7); // Shaman
        character4.setLevel(60);
        character4.setRankByInt(3); // Rank 4
        character4.setRaceByID(28); // Highmountain
        character4.setBlizzardID("126");
        character4.setCanonicalID(126);
        character4.setIconURL("https://api.wowguild.com/icon/126");
        character4.setRegionEn("NA");
        character4.setRanks(null);
        characterList.add(character4);

        Character character5 = new Character();
        character5.setId(127);
        character5.setName("Fenthis");
        character5.setClassEnByInt(9); // Warlock
        character5.setLevel(60);
        character5.setRankByInt(4); // Rank 5
        character5.setRaceByID(10); // Blood Elf
        character5.setBlizzardID("127");
        character5.setCanonicalID(127);
        character5.setIconURL("https://api.wowguild.com/icon/127");
        character5.setRegionEn("NA");
        character5.setRanks(null);
        characterList.add(character5);

        return characterList;
    }

    private static GuildProfile.Member createMember(String characterName, long characterId, int level,
                                                    int classId, int raceId, int rank) {
        GuildProfile.Member member = new GuildProfile.Member();

        // Create Character
        GuildProfile.Character character = new GuildProfile.Character();
        GuildProfile.Key characterKey = new GuildProfile.Key();
        characterKey.setHref("https://api.wowguild.com/character/" + characterId);
        character.setKey(characterKey);
        character.setName(characterName);
        character.setId(characterId);
        character.setLevel(level);

        // Set Playable Class
        GuildProfile.PlayableClass playableClass = new GuildProfile.PlayableClass();
        GuildProfile.Key classKey = new GuildProfile.Key();
        classKey.setHref("https://api.wowguild.com/class");
        playableClass.setKey(classKey);
        playableClass.setId(classId);
        character.setPlayable_class(playableClass);

        // Set Playable Race
        GuildProfile.PlayableRace playableRace = new GuildProfile.PlayableRace();
        GuildProfile.Key raceKey = new GuildProfile.Key();
        raceKey.setHref("https://api.wowguild.com/race");
        playableRace.setKey(raceKey);
        playableRace.setId(raceId);
        character.setPlayable_race(playableRace);

        // Set Realm
        GuildProfile.Realm characterRealm = new GuildProfile.Realm();
        GuildProfile.Key realmKey = new GuildProfile.Key();
        realmKey.setHref("https://api.wowguild.com/realm");
        characterRealm.setKey(realmKey);
        characterRealm.setName("Stormrage");
        characterRealm.setId(567);
        characterRealm.setSlug("stormrage");
        character.setRealm(characterRealm);

        // Assign character to member and set rank
        member.setCharacter(character);
        member.setRank(rank);

        return member;
    }
}
