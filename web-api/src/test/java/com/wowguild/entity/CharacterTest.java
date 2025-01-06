package com.wowguild.entity;

import com.wowguild.common.entity.wow.Character;
import com.wowguild.common.enums.wow.ClassEn;
import com.wowguild.common.enums.wow.GuildRank;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.wowguild.arguments.CharacterVarArguments.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTest {

    @Test
    void setClassEnByIntTest() {
        List<Variables<ClassEn>> characterClassArguments = getCharacterClassArguments();
        Character testCharacter = new Character();
        for (Variables<ClassEn> characterClassArgument : characterClassArguments) {
            testCharacter.setClassEnByInt(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getClassEn());
        }

    }

    @Test
    void setRankByIntTest() {
        List<Variables<GuildRank>> characterClassArguments = getCharacterRankArguments();
        Character testCharacter = new Character();
        for (Variables<GuildRank> characterClassArgument : characterClassArguments) {
            testCharacter.setRankByInt(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getGuildRank());
        }

    }

    @Test
    void setRaceByIDTest() {
        List<Variables<String>> characterClassArguments = getCharacterRaceArguments();
        Character testCharacter = new Character();
        for (Variables<String> characterClassArgument : characterClassArguments) {
            testCharacter.setRaceByID(characterClassArgument.getIntValue());

            assertEquals(characterClassArgument.getValue(), testCharacter.getRace());
        }

    }

}
