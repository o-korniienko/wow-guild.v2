package com.wowguild.arguments;

import com.wowguild.common.enums.wow.ClassEn;
import com.wowguild.common.enums.wow.GuildRank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class CharacterVarArguments {

    public static List<Variables<ClassEn>> getCharacterClassArguments() {
        List<Variables<ClassEn>> variablesList = new ArrayList<>();

        Variables<ClassEn> variables1 = new Variables<>(1, ClassEn.Warrior);
        Variables<ClassEn> variables2 = new Variables<>(2, ClassEn.Paladin);
        Variables<ClassEn> variables3 = new Variables<>(3, ClassEn.Hunter);
        Variables<ClassEn> variables4 = new Variables<>(4, ClassEn.Rogue);
        Variables<ClassEn> variables5 = new Variables<>(5, ClassEn.Priest);
        Variables<ClassEn> variables6 = new Variables<>(6, ClassEn.DeathKnight);
        Variables<ClassEn> variables7 = new Variables<>(7, ClassEn.Shaman);
        Variables<ClassEn> variables8 = new Variables<>(8, ClassEn.Mage);
        Variables<ClassEn> variables9 = new Variables<>(9, ClassEn.Warlock);
        Variables<ClassEn> variables10 = new Variables<>(10, ClassEn.Monk);
        Variables<ClassEn> variables11 = new Variables<>(11, ClassEn.Druid);
        Variables<ClassEn> variables12 = new Variables<>(12, ClassEn.DemonHunter);
        Variables<ClassEn> variables13 = new Variables<>(13, ClassEn.Evoker);

        variablesList.add(variables1);
        variablesList.add(variables2);
        variablesList.add(variables3);
        variablesList.add(variables4);
        variablesList.add(variables5);
        variablesList.add(variables6);
        variablesList.add(variables7);
        variablesList.add(variables8);
        variablesList.add(variables9);
        variablesList.add(variables10);
        variablesList.add(variables11);
        variablesList.add(variables12);
        variablesList.add(variables13);


        return variablesList;
    }

    public static List<Variables<GuildRank>> getCharacterRankArguments() {
        List<Variables<GuildRank>> variablesList = new ArrayList<>();

        Variables<GuildRank> variables0 = new Variables<>(0, GuildRank.Guild_Master);
        Variables<GuildRank> variables1 = new Variables<>(1, GuildRank.Rank2);
        Variables<GuildRank> variables2 = new Variables<>(2, GuildRank.Rank3);
        Variables<GuildRank> variables3 = new Variables<>(3, GuildRank.Rank4);
        Variables<GuildRank> variables4 = new Variables<>(4, GuildRank.Rank5);
        Variables<GuildRank> variables5 = new Variables<>(5, GuildRank.Rank6);
        Variables<GuildRank> variables6 = new Variables<>(6, GuildRank.Rank7);
        Variables<GuildRank> variables7 = new Variables<>(7, GuildRank.Rank8);
        Variables<GuildRank> variables8 = new Variables<>(8, GuildRank.Rank9);
        Variables<GuildRank> variables9 = new Variables<>(9, GuildRank.Rank10);

        variablesList.add(variables0);
        variablesList.add(variables1);
        variablesList.add(variables2);
        variablesList.add(variables3);
        variablesList.add(variables4);
        variablesList.add(variables5);
        variablesList.add(variables6);
        variablesList.add(variables7);
        variablesList.add(variables8);
        variablesList.add(variables9);

        return variablesList;
    }

    public static List<Variables<String>> getCharacterRaceArguments() {
        List<Variables<String>> variablesList = new ArrayList<>();

        Variables<String> variables0 = new Variables<>(5, "Undead");
        Variables<String> variables1 = new Variables<>(8, "Troll");
        Variables<String> variables2 = new Variables<>(27, "Nightborne");
        Variables<String> variables3 = new Variables<>(28, "Highmountain");
        Variables<String> variables4 = new Variables<>(10, "Blood Elf");
        Variables<String> variables5 = new Variables<>(26, "Pandaren");
        Variables<String> variables6 = new Variables<>(2, "Orc");
        Variables<String> variables7 = new Variables<>(31, "Zandalari Troll");
        Variables<String> variables8 = new Variables<>(9, "Goblin");
        Variables<String> variables9 = new Variables<>(6, "Tauren");
        Variables<String> variables10 = new Variables<>(35, "Vulpera");
        Variables<String> variables11 = new Variables<>(36, "Mag'har Orc");

        variablesList.add(variables0);
        variablesList.add(variables1);
        variablesList.add(variables2);
        variablesList.add(variables3);
        variablesList.add(variables4);
        variablesList.add(variables5);
        variablesList.add(variables6);
        variablesList.add(variables7);
        variablesList.add(variables8);
        variablesList.add(variables9);
        variablesList.add(variables10);
        variablesList.add(variables11);

        return variablesList;
    }

    @Data
    public static class Variables<T> {

        private int intValue;
        private T value;

        public Variables(int intValue, T value) {
            this.intValue = intValue;
            this.value = value;
        }
    }
}
