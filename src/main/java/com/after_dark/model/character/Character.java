package com.after_dark.model.character;

import com.after_dark.model.rank.CharacterRank;
import com.after_dark.model.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private ClassEn classEn;
    private int level;
    private Rank rank;
    private String race;
    private String iconURL;
    private String regionEn;
    private String blizzardID;
    private long canonicalID;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "character_id")
    private List<CharacterRank> ranks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassEn getClassEn() {
        return classEn;
    }

    public void setClassEn(ClassEn classEn) {
        this.classEn = classEn;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getRegionEn() {
        return regionEn;
    }

    public void setRegionEn(String regionEn) {
        this.regionEn = regionEn;
    }


    public long getCanonicalID() {
        return canonicalID;
    }

    public void setCanonicalID(long canonicalID) {
        this.canonicalID = canonicalID;
    }

    public List<CharacterRank> getRanks() {
        return ranks;
    }

    public void setRanks(List<CharacterRank> ranks) {
        this.ranks = ranks;
    }

    public String getBlizzardID() {
        return blizzardID;
    }

    public void setBlizzardID(String blizzardID) {
        this.blizzardID = blizzardID;
    }

    public void setClassEnByInt(int classInt) {
        switch (classInt) {
            case 1:
                this.classEn = ClassEn.Warrior;
                break;
            case 2:
                this.classEn = ClassEn.Paladin;
                break;
            case 3:
                this.classEn = ClassEn.Hunter;
                break;
            case 4:
                this.classEn = ClassEn.Rogue;
                break;
            case 5:
                this.classEn = ClassEn.Priest;
                break;
            case 6:
                this.classEn = ClassEn.DeathKnight;
                break;
            case 7:
                this.classEn = ClassEn.Shaman;
                break;
            case 8:
                this.classEn = ClassEn.Mage;
                break;
            case 9:
                this.classEn = ClassEn.Warlock;
                break;
            case 10:
                this.classEn = ClassEn.Monk;
                break;
            case 11:
                this.classEn = ClassEn.Druid;
                break;
            case 12:
                this.classEn = ClassEn.DemonHunter;
                break;
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id &&
                level == character.level &&
                Objects.equals(name, character.name) &&
                classEn == character.classEn &&
                rank == character.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, classEn, level, rank);
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classEn=" + classEn +
                ", level=" + level +
                ", rank=" + rank +
                ", race='" + race + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", regionEn='" + regionEn + '\'' +
                ", blizzardID=" + blizzardID +
                ", cannonicalID=" + canonicalID +
                ", user=" + user +
                ", ranks=" + ranks +
                '}';
    }

    public void setRankByInt(int parseInt) {
        switch (parseInt) {
            case 0:
                this.rank = Rank.Guild_Master;
                break;
            case 1:
                this.rank = Rank.Rank2;
                break;

            case 2:
                this.rank = Rank.Rank3;
                break;
            case 3:
                this.rank = Rank.Rank4;
                break;
            case 4:
                this.rank = Rank.Rank5;
                break;
            case 5:
                this.rank = Rank.Rank6;
                break;
            case 6:
                this.rank = Rank.Rank7;
                break;
            case 7:
                this.rank = Rank.Rank8;
                break;
            case 8:
                this.rank = Rank.Rank9;
                break;
            case 9:
                this.rank = Rank.Rank10;
                break;
        }

    }

    public void setRaceByID(int parseInt) {
        switch (parseInt) {
            case 5:
                this.race = "Undead";
                break;
            case 8:
                this.race = "Troll";
                break;

            case 27:
                this.race = "Nightborne";
                break;
            case 28:
                this.race = "Highmountain";
                break;
            case 10:
                this.race = "Blood Elf";
                break;
            case 26:
                this.race = "Pandaren";
                break;
            case 2:
                this.race = "Orc";
                break;
            case 31:
                this.race = "Zandalari Troll";
                break;
            case 9:
                this.race = "Goblin";
                break;
            case 6:
                this.race = "Tauren";
                break;
            case 35:
                this.race = "Vulpera";
                break;
            case 36:
                this.race = "Mag'har Orc";
                break;

        }

    }
}
