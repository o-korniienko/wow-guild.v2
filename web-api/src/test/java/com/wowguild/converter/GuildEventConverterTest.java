package com.wowguild.converter;

import com.wowguild.common.converter.GuildEventConverter;
import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.entity.GuildEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.wowguild.arguments.GuildEventGenerator.generateGuildEvent;
import static com.wowguild.arguments.GuildEventGenerator.generateGuildEventDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GuildEventConverterTest {

    private GuildEventConverter guildEventConverter;
    private GuildEvent eventExpected;
    private GuildEventDto eventDtoExpected;
    private final static LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        guildEventConverter = new GuildEventConverter();
        eventExpected = generateGuildEvent("subject", 1, "message", now);
        eventDtoExpected = generateGuildEventDto("subject", 1, "message", now);
    }

    @ParameterizedTest
    @MethodSource("convertToDtoArgs")
    public void convertToDtoTest(GuildEvent event, boolean positiveTest) {
        GuildEventDto result = guildEventConverter.convertToDto(event);
        if (positiveTest) {
            assertEquals(eventDtoExpected, result);
        } else {
            assertNotEquals(eventDtoExpected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("convertToEntityArgs")
    public void convertToEntityTest(GuildEventDto event, boolean positiveTest) {
        GuildEvent result = guildEventConverter.convertToEntity(event);
        if (positiveTest) {
            assertEquals(eventExpected, result);
        } else {
            assertNotEquals(eventExpected, result);
        }
    }


    static Stream<Arguments> convertToDtoArgs() {
        GuildEvent event1 = generateGuildEvent("subject", 1, "message", now);
        GuildEvent event2 = generateGuildEvent("subject1", 1, "message", now);
        GuildEvent event3 = generateGuildEvent("subject", 2, "message", now);
        GuildEvent event4 = generateGuildEvent("subject", 1, "message1", now);
        GuildEvent event5 = generateGuildEvent("subject", 1, "message",
                LocalDateTime.now().plusSeconds(300));

        return Stream.of(
                Arguments.of(event1, true),
                Arguments.of(event2, false),
                Arguments.of(event3, false),
                Arguments.of(event4, false),
                Arguments.of(event5, false)
        );
    }

    static Stream<Arguments> convertToEntityArgs() {
        GuildEventDto event1 = generateGuildEventDto("subject", 1, "message", now);
        GuildEventDto event2 = generateGuildEventDto("subject1", 1, "message", now);
        GuildEventDto event3 = generateGuildEventDto("subject", 2, "message", now);
        GuildEventDto event4 = generateGuildEventDto("subject", 1, "message1", now);
        GuildEventDto event5 = generateGuildEventDto("subject", 1, "message",
                LocalDateTime.now().plusSeconds(300));

        return Stream.of(
                Arguments.of(event1, true),
                Arguments.of(event2, false),
                Arguments.of(event3, false),
                Arguments.of(event4, false),
                Arguments.of(event5, false)
        );
    }
}
