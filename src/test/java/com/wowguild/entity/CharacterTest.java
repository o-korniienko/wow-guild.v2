package com.wowguild.entity;

import com.wowguild.arguments.CharacterVarArguments;
import com.wowguild.enums.ClassEn;
import com.wowguild.enums.Rank;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.wowguild.arguments.CharacterVarArguments.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTest {

    @Test
    void setClassEnByIntTest() {
        List<CharacterVarArguments.Variables<ClassEn>> characterClassArguments = getCharacterClassArguments();
        Character testCharacter = new Character();
        for (CharacterVarArguments.Variables<ClassEn> characterClassArgument : characterClassArguments) {
            testCharacter.setClassEnByInt(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getClassEn());
        }

    }

    @Test
    void setRankByIntTest() {
        List<CharacterVarArguments.Variables<Rank>> characterClassArguments = getCharacterRankArguments();
        Character testCharacter = new Character();
        for (CharacterVarArguments.Variables<Rank> characterClassArgument : characterClassArguments) {
            testCharacter.setRankByInt(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getRank());
        }

    }

    @Test
    void setRaceByIDTest() {
        List<CharacterVarArguments.Variables<String>> characterClassArguments = getCharacterRaceArguments();
        Character testCharacter = new Character();
        for (CharacterVarArguments.Variables<String> characterClassArgument : characterClassArguments) {
            testCharacter.setRaceByID(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getRace());
        }

    }

}
