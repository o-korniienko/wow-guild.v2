package com.after_dark.model.wow_logs_models;

import java.util.List;

public class WOWLogsFightData {
    private List<Fight> fights;
    private Zone zone;
    private List<RankedCharacter> rankedCharacters;

    public List<Fight> getFights() {
        return fights;
    }

    public void setFights(List<Fight> fights) {
        this.fights = fights;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<RankedCharacter> getRankedCharacters() {
        return rankedCharacters;
    }

    public void setRankedCharacters(List<RankedCharacter> rankedCharacters) {
        this.rankedCharacters = rankedCharacters;
    }

    @Override
    public String toString() {
        return "WOWLogsFightData{" +
                "fights=" + fights +
                ", zone=" + zone +
                ", rankedCharacters=" + rankedCharacters +
                '}';
    }

    public class Fight {
        private boolean kill;
        private String name;
        private int difficulty;
        private long encounterID;

        public boolean isKill() {
            return kill;
        }

        public void setKill(boolean kill) {
            this.kill = kill;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getEncounterID() {
            return encounterID;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public void setEncounterID(long encounterID) {
            this.encounterID = encounterID;
        }

        @Override
        public String toString() {
            return "Fight{" +
                    "kill=" + kill +
                    ", name='" + name + '\'' +
                    ", encounterID=" + encounterID +
                    ", difficulty=" + difficulty +
                    '}';
        }
    }

    public class Zone {

        private String name;
        private Expansion expansion;
        private Bracket brackets;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Expansion getExpansion() {
            return expansion;
        }

        public void setExpansion(Expansion expansion) {
            this.expansion = expansion;
        }

        public Bracket getBrackets() {
            return brackets;
        }

        public void setBrackets(Bracket brackets) {
            this.brackets = brackets;
        }

        @Override
        public String toString() {
            return "Zone{" +
                    "name='" + name + '\'' +
                    ", expansion=" + expansion +
                    ", brackets=" + brackets +
                    '}';
        }

        public class Expansion{
            private String name;

            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Expansion{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

        public class Bracket{
            private String type;
            private int bucket;
            private int min;
            private int max;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getBucket() {
                return bucket;
            }

            public void setBucket(int bucket) {
                this.bucket = bucket;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            @Override
            public String toString() {
                return "Bracket{" +
                        "type='" + type + '\'' +
                        ", bucket=" + bucket +
                        ", min=" + min +
                        ", max=" + max +
                        '}';
            }
        }
    }

    public class RankedCharacter {

        private String name;
        private long canonicalID;
        private Server server;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCanonicalID() {
            return canonicalID;
        }

        public void setCanonicalID(long canonicalID) {
            this.canonicalID = canonicalID;
        }

        public Server getServer() {
            return server;
        }

        public void setServer(Server server) {
            this.server = server;
        }

        @Override
        public String toString() {
            return "RankedCharacter{" +
                    "name='" + name + '\'' +
                    "cannonicalID='" + canonicalID + '\'' +
                    "server='" + server + '\'' +
                    '}';
        }
    }

    public class Server{
        private String slug;

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        @Override
        public String toString() {
            return "Server{" +
                    "slug='" + slug + '\'' +
                    '}';
        }
    }
}
