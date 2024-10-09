package com.wowguild.arguments;

import com.wowguild.enums.ClassEn;
import com.wowguild.enums.Rank;
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

    public static List<Variables<Rank>> getCharacterRankArguments() {
        List<Variables<Rank>> variablesList = new ArrayList<>();

        Variables<Rank> variables0 = new Variables<>(0, Rank.Guild_Master);
        Variables<Rank> variables1 = new Variables<>(1, Rank.Rank2);
        Variables<Rank> variables2 = new Variables<>(2, Rank.Rank3);
        Variables<Rank> variables3 = new Variables<>(3, Rank.Rank4);
        Variables<Rank> variables4 = new Variables<>(4, Rank.Rank5);
        Variables<Rank> variables5 = new Variables<>(5, Rank.Rank6);
        Variables<Rank> variables6 = new Variables<>(6, Rank.Rank7);
        Variables<Rank> variables7 = new Variables<>(7, Rank.Rank8);
        Variables<Rank> variables8 = new Variables<>(8, Rank.Rank9);
        Variables<Rank> variables9 = new Variables<>(9, Rank.Rank10);

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
