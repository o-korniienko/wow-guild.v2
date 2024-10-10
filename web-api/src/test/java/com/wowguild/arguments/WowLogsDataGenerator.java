package com.wowguild.arguments;

import com.wowguild.model.wow_logs.WOWLogsCharacterRankData;
import com.wowguild.model.wow_logs.WOWLogsFightData;
import com.wowguild.model.wow_logs.WOWLogsReportData;

import java.util.ArrayList;
import java.util.List;

public class WowLogsDataGenerator {

    public static String getWowLogsReportsJson() {
        return """
                {
                  "data": {
                    "reportData": {
                        "reports": {
                            "data": [
                              {
                                "code": "ABC123",
                                "endTime": 1633024800000
                              },
                              {
                                "code": "DEF456",
                                "endTime": 1633111200000
                              },
                              {
                                "code": "GHI789",
                                "endTime": 1633197600000
                              },
                              {
                                "code": "JKL012",
                                "endTime": 1633284000000
                              },
                              {
                                "code": "MNO345",
                                "endTime": 1633370400000
                              },
                              {
                                "code": "PQR678",
                                "endTime": 1633456800000
                              },
                              {
                                "code": "STU901",
                                "endTime": 1633543200000
                              }
                            ]
                        }
                    }
                  }
                }
                """;
    }

    public static WOWLogsReportData getWowLogsReportsObject() {
        WOWLogsReportData wowLogsReportData = new WOWLogsReportData();
        List<WOWLogsReportData.ReportDto> reportList = new ArrayList<>();

        WOWLogsReportData.ReportDto report1 = new WOWLogsReportData.ReportDto();
        report1.setCode("ABC123");
        report1.setEndTime(1633024800000L);
        reportList.add(report1);

        WOWLogsReportData.ReportDto report2 = new WOWLogsReportData.ReportDto();
        report2.setCode("DEF456");
        report2.setEndTime(1633111200000L);
        reportList.add(report2);

        WOWLogsReportData.ReportDto report3 = new WOWLogsReportData.ReportDto();
        report3.setCode("GHI789");
        report3.setEndTime(1633197600000L);
        reportList.add(report3);

        WOWLogsReportData.ReportDto report4 = new WOWLogsReportData.ReportDto();
        report4.setCode("JKL012");
        report4.setEndTime(1633284000000L);
        reportList.add(report4);

        WOWLogsReportData.ReportDto report5 = new WOWLogsReportData.ReportDto();
        report5.setCode("MNO345");
        report5.setEndTime(1633370400000L);
        reportList.add(report5);

        WOWLogsReportData.ReportDto report6 = new WOWLogsReportData.ReportDto();
        report6.setCode("PQR678");
        report6.setEndTime(1633456800000L);
        reportList.add(report6);

        WOWLogsReportData.ReportDto report7 = new WOWLogsReportData.ReportDto();
        report7.setCode("STU901");
        report7.setEndTime(1633543200000L);
        reportList.add(report7);

        wowLogsReportData.setData(reportList);

        return wowLogsReportData;
    }

    public static String getFightReportDataJson() {
        return """
                {
                    "data": {
                        "reportData": {
                            "report": {
                                "fights": [
                                    {
                                      "kill": true,
                                      "name": "Fight 1",
                                      "difficulty": 3,
                                      "encounterID": 1001,
                                      "gameZone": {
                                        "name": "Zone A",
                                        "id": 201
                                      }
                                    },
                                    {
                                      "kill": false,
                                      "name": "Fight 2",
                                      "difficulty": 4,
                                      "encounterID": 1002,
                                      "gameZone": {
                                        "name": "Zone B",
                                        "id": 202
                                      }
                                    },
                                    {
                                      "kill": true,
                                      "name": "Fight 3",
                                      "difficulty": 5,
                                      "encounterID": 1003,
                                      "gameZone": {
                                        "name": "Zone C",
                                        "id": 203
                                      }
                                    }
                                ],
                                "zone": {
                                  "name": "Mythic Dungeon",
                                  "expansion": {
                                    "name": "Shadowlands"
                                  },
                                  "brackets": {
                                    "type": "Mythic",
                                    "bucket": 1,
                                    "min": 10,
                                    "max": 15
                                  }
                                },
                                "rankedCharacters": [
                                  {
                                    "name": "Character1",
                                    "canonicalID": 3001,
                                    "server": {
                                      "slug": "server-1"
                                    }
                                  },
                                  {
                                    "name": "Character2",
                                    "canonicalID": 3002,
                                    "server": {
                                      "slug": "server-2"
                                    }
                                  },
                                  {
                                    "name": "Character3",
                                    "canonicalID": 3003,
                                    "server": {
                                      "slug": "server-3"
                                    }
                                  },
                                  {
                                    "name": "Character4",
                                    "canonicalID": 3004,
                                    "server": {
                                      "slug": "server-4"
                                    }
                                  },
                                  {
                                    "name": "Character5",
                                    "canonicalID": 3005,
                                    "server": {
                                      "slug": "server-5"
                                    }
                                  },
                                  {
                                    "name": "Character6",
                                    "canonicalID": 3006,
                                    "server": {
                                      "slug": "server-6"
                                    }
                                  },
                                  {
                                    "name": "Character7",
                                    "canonicalID": 3007,
                                    "server": {
                                      "slug": "server-7"
                                    }
                                  },
                                  {
                                    "name": "Character8",
                                    "canonicalID": 3008,
                                    "server": {
                                      "slug": "server-8"
                                    }
                                  },
                                  {
                                    "name": "Character9",
                                    "canonicalID": 3009,
                                    "server": {
                                      "slug": "server-9"
                                    }
                                  },
                                  {
                                    "name": "Character10",
                                    "canonicalID": 3010,
                                    "server": {
                                      "slug": "server-10"
                                    }
                                  }
                                ]
                            }
                        }
                    }
                }
                """;
    }

    public static WOWLogsFightData getFightReportDataObject() {
        WOWLogsFightData wowLogsFightData = new WOWLogsFightData();

        List<WOWLogsFightData.Fight> fights = new ArrayList<>();

        WOWLogsFightData.Fight fight1 = new WOWLogsFightData.Fight();
        fight1.setKill(true);
        fight1.setName("Fight 1");
        fight1.setDifficulty(3);
        fight1.setEncounterID(1001);

        WOWLogsFightData.Fight.FightZone fightZone1 = new WOWLogsFightData.Fight.FightZone();
        fightZone1.setName("Zone A");
        fightZone1.setId(201);
        fight1.setGameZone(fightZone1);
        fights.add(fight1);

        WOWLogsFightData.Fight fight2 = new WOWLogsFightData.Fight();
        fight2.setKill(false);
        fight2.setName("Fight 2");
        fight2.setDifficulty(4);
        fight2.setEncounterID(1002);

        WOWLogsFightData.Fight.FightZone fightZone2 = new WOWLogsFightData.Fight.FightZone();
        fightZone2.setName("Zone B");
        fightZone2.setId(202);
        fight2.setGameZone(fightZone2);
        fights.add(fight2);

        WOWLogsFightData.Fight fight3 = new WOWLogsFightData.Fight();
        fight3.setKill(true);
        fight3.setName("Fight 3");
        fight3.setDifficulty(5);
        fight3.setEncounterID(1003);

        WOWLogsFightData.Fight.FightZone fightZone3 = new WOWLogsFightData.Fight.FightZone();
        fightZone3.setName("Zone C");
        fightZone3.setId(203);
        fight3.setGameZone(fightZone3);
        fights.add(fight3);

        wowLogsFightData.setFights(fights);

        // Create Zone object
        WOWLogsFightData.Zone zone = new WOWLogsFightData.Zone();
        zone.setName("Mythic Dungeon");

        WOWLogsFightData.Zone.Expansion expansion = new WOWLogsFightData.Zone.Expansion();
        expansion.setName("Shadowlands");
        zone.setExpansion(expansion);

        WOWLogsFightData.Zone.Bracket bracket = new WOWLogsFightData.Zone.Bracket();
        bracket.setType("Mythic");
        bracket.setBucket(1);
        bracket.setMin(10);
        bracket.setMax(15);
        zone.setBrackets(bracket);

        wowLogsFightData.setZone(zone);

        // Create RankedCharacter objects
        List<WOWLogsFightData.RankedCharacter> rankedCharacters = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            WOWLogsFightData.RankedCharacter rankedCharacter = new WOWLogsFightData.RankedCharacter();
            rankedCharacter.setName("Character" + i);
            rankedCharacter.setCanonicalID(3000 + i);

            WOWLogsFightData.RankedCharacter.Server server = new WOWLogsFightData.RankedCharacter.Server();
            server.setSlug("server-" + i);
            rankedCharacter.setServer(server);

            rankedCharacters.add(rankedCharacter);
        }

        wowLogsFightData.setRankedCharacters(rankedCharacters);

        return wowLogsFightData;
    }

    public static String getCharacterRankDataJson() {
        return """
                {
                    "data": {
                        "characterData": {
                            "character": {
                                "encounterRankings": {
                                  "bestAmount": 500.5,
                                  "medianPerformance": 85.3,
                                  "averagePerformance": 90.7,
                                  "totalKills": 25,
                                  "fastestKill": 300,
                                  "difficulty": 5,
                                  "metric": "dps",
                                  "partition": 2,
                                  "zone": 123,
                                  "ranks": [
                                    {
                                      "lockedIn": true,
                                      "rankPercent": 95.6,
                                      "historicalPercent": 92.4,
                                      "todayPercent": 88.2,
                                      "rankTotalParses": 1000,
                                      "historicalTotalParses": 900,
                                      "todayTotalParses": 800,
                                      "guild": {
                                        "id": 1,
                                        "name": "Warriors of Light",
                                        "faction": 0
                                      },
                                      "report": {
                                        "code": "ABC123",
                                        "startTime": 1617970800000,
                                        "fightID": 5678
                                      },
                                      "duration": 1200,
                                      "startTime": 1617970800000,
                                      "amount": 450.2,
                                      "bracketData": 3,
                                      "spec": "Arms",
                                      "bestSpec": "Arms",
                                      "classType": 1,
                                      "faction": 0
                                    },
                                    {
                                      "lockedIn": false,
                                      "rankPercent": 85.9,
                                      "historicalPercent": 84.1,
                                      "todayPercent": 79.5,
                                      "rankTotalParses": 1100,
                                      "historicalTotalParses": 950,
                                      "todayTotalParses": 820,
                                      "guild": {
                                        "id": 2,
                                        "name": "Shadow Council",
                                        "faction": 1
                                      },
                                      "report": {
                                        "code": "DEF456",
                                        "startTime": 1617973800000,
                                        "fightID": 5679
                                      },
                                      "duration": 1400,
                                      "startTime": 1617973800000,
                                      "amount": 400.7,
                                      "bracketData": 2,
                                      "spec": "Protection",
                                      "bestSpec": "Protection",
                                      "classType": 1,
                                      "faction": 1
                                    }
                                  ]
                                }
                            }
                        }
                    }
                }
                """;
    }

    public static WOWLogsCharacterRankData getCharacterRankDataObject() {
        WOWLogsCharacterRankData rankData = new WOWLogsCharacterRankData();
        WOWLogsCharacterRankData.CharacterRankings encounterRankings = new WOWLogsCharacterRankData.CharacterRankings();

        // Set basic encounter ranking details
        encounterRankings.setBestAmount(500.5);
        encounterRankings.setMedianPerformance(85.3);
        encounterRankings.setAveragePerformance(90.7);
        encounterRankings.setTotalKills(25);
        encounterRankings.setFastestKill(300);
        encounterRankings.setDifficulty(5);
        encounterRankings.setMetric("dps");
        encounterRankings.setPartition(2);
        encounterRankings.setZone(123);

        // Create the list of ranks
        List<WOWLogsCharacterRankData.CharacterRankings.Rank> ranks = new ArrayList<>();

        // First rank
        WOWLogsCharacterRankData.CharacterRankings.Rank rank1 = new WOWLogsCharacterRankData.CharacterRankings.Rank();
        rank1.setLockedIn(true);
        rank1.setRankPercent(95.6);
        rank1.setHistoricalPercent(92.4);
        rank1.setTodayPercent(88.2);
        rank1.setRankTotalParses(1000);
        rank1.setHistoricalTotalParses(900);
        rank1.setTodayTotalParses(800);
        rank1.setDuration(1200);
        rank1.setStartTime(1617970800000L);
        rank1.setAmount(450.2);
        rank1.setBracketData(3);
        rank1.setSpec("Arms");
        rank1.setBestSpec("Arms");
        rank1.setClassType(1);
        rank1.setFaction(0);

        // Set guild for rank1
        WOWLogsCharacterRankData.CharacterRankings.Rank.Guild guild1 = new WOWLogsCharacterRankData.CharacterRankings.Rank.Guild();
        guild1.setId(1);
        guild1.setName("Warriors of Light");
        guild1.setFaction(0);
        rank1.setGuild(guild1);

        // Set report for rank1
        WOWLogsCharacterRankData.CharacterRankings.Rank.Report report1 = new WOWLogsCharacterRankData.CharacterRankings.Rank.Report();
        report1.setCode("ABC123");
        report1.setStartTime(1617970800000L);
        report1.setFightID(5678);
        rank1.setReport(report1);

        // Add rank1 to ranks list
        ranks.add(rank1);

        // Second rank
        WOWLogsCharacterRankData.CharacterRankings.Rank rank2 = new WOWLogsCharacterRankData.CharacterRankings.Rank();
        rank2.setLockedIn(false);
        rank2.setRankPercent(85.9);
        rank2.setHistoricalPercent(84.1);
        rank2.setTodayPercent(79.5);
        rank2.setRankTotalParses(1100);
        rank2.setHistoricalTotalParses(950);
        rank2.setTodayTotalParses(820);
        rank2.setDuration(1400);
        rank2.setStartTime(1617973800000L);
        rank2.setAmount(400.7);
        rank2.setBracketData(2);
        rank2.setSpec("Protection");
        rank2.setBestSpec("Protection");
        rank2.setClassType(1);
        rank2.setFaction(1);

        // Set guild for rank2
        WOWLogsCharacterRankData.CharacterRankings.Rank.Guild guild2 = new WOWLogsCharacterRankData.CharacterRankings.Rank.Guild();
        guild2.setId(2);
        guild2.setName("Shadow Council");
        guild2.setFaction(1);
        rank2.setGuild(guild2);

        // Set report for rank2
        WOWLogsCharacterRankData.CharacterRankings.Rank.Report report2 = new WOWLogsCharacterRankData.CharacterRankings.Rank.Report();
        report2.setCode("DEF456");
        report2.setStartTime(1617973800000L);
        report2.setFightID(5679);
        rank2.setReport(report2);

        // Add rank2 to ranks list
        ranks.add(rank2);

        // Set ranks in encounterRankings
        encounterRankings.setRanks(ranks);

        // Set encounter rankings in WOWLogsCharacterRankData
        rankData.setEncounterRankings(encounterRankings);

        return rankData;
    }

}
