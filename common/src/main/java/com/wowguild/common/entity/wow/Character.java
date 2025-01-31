package com.wowguild.common.entity.wow;

import com.wowguild.common.entity.wow.rank.CharacterRank;
import com.wowguild.common.enums.wow.ClassEn;
import com.wowguild.common.enums.wow.GuildRank;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(exclude = {"race", "iconURL", "regionEn", "blizzardID", "canonicalID"})
@Data
@Entity
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private ClassEn classEn;
    private int level;
    private GuildRank guildRank;
    private String race;
    private String iconURL;
    private String regionEn;
    private String blizzardID;
    private long canonicalID;
    @OneToMany
    @JoinColumn(name = "character_id")
    private List<CharacterRank> ranks;

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
            case 13:
                this.classEn = ClassEn.Evoker;
                break;
        }
    }

    public void setRankByInt(int parseInt) {
        switch (parseInt) {
            case 0:
                this.guildRank = GuildRank.Guild_Master;
                break;
            case 1:
                this.guildRank = GuildRank.Rank2;
                break;

            case 2:
                this.guildRank = GuildRank.Rank3;
                break;
            case 3:
                this.guildRank = GuildRank.Rank4;
                break;
            case 4:
                this.guildRank = GuildRank.Rank5;
                break;
            case 5:
                this.guildRank = GuildRank.Rank6;
                break;
            case 6:
                this.guildRank = GuildRank.Rank7;
                break;
            case 7:
                this.guildRank = GuildRank.Rank8;
                break;
            case 8:
                this.guildRank = GuildRank.Rank9;
                break;
            case 9:
                this.guildRank = GuildRank.Rank10;
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

    public boolean isRaider() {
        return switch (this.guildRank) {
            case Guild_Master, Rank2, Rank3, Rank4 -> true;
            default -> false;
        };

    }
}
