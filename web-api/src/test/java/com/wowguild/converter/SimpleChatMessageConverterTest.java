package com.wowguild.converter;

import com.wowguild.common.converter.SimpleChatMessageConverter;
import com.wowguild.common.dto.SimpleChatMessageDto;
import com.wowguild.common.entity.SimpleChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleChatMessageConverterTest {

    private SimpleChatMessageConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new SimpleChatMessageConverter();
    }

    @ParameterizedTest
    @MethodSource("testConvertToArgs")
    void testConvertToDto(SimpleChatMessage chatMessage, SimpleChatMessageDto expected) {
        SimpleChatMessageDto result = converter.convertToDto(chatMessage);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("testConvertToArgs")
    public void testConvertToEntity(SimpleChatMessage expected, SimpleChatMessageDto chatMessageDto) {
        SimpleChatMessage result = converter.convertToEntity(chatMessageDto);
        assertEquals(expected, result);
    }

    static Stream<Arguments> testConvertToArgs() {
        LocalDateTime now = LocalDateTime.now();
        SimpleChatMessage chatMessage = new SimpleChatMessage();
        chatMessage.setMessage("some message");
        chatMessage.setId(123);
        chatMessage.setDate(now);
        chatMessage.setUserId(432);

        SimpleChatMessageDto chatMessageDto = new SimpleChatMessageDto();
        chatMessageDto.setMessage("some message");
        chatMessageDto.setId(123);
        chatMessageDto.setDate(now);
        chatMessageDto.setUserId(432);

        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(chatMessage, chatMessageDto)
        );
    }
}
