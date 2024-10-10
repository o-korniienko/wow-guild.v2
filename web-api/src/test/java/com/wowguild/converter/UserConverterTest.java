package com.wowguild.converter;

import com.wowguild.dto.UserDto;
import com.wowguild.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.wowguild.arguments.UserGenerator.generateUser;
import static com.wowguild.arguments.UserGenerator.generateUserDto;
import static org.junit.jupiter.api.Assertions.*;

public class UserConverterTest {

    private UserConverter userConverter;
    private UserDto userDtoExpected;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();
        userDtoExpected = generateUserDto("tom");
    }

    @ParameterizedTest
    @MethodSource("provideUserTestArgs")
    void testConvertToDto(User user, boolean positiveTest) {
        UserDto result = userConverter.convertToDto(user);
        if (positiveTest) {
            assertEquals(userDtoExpected, result);
        } else {
            assertNotEquals(userDtoExpected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("provideUserDtoTestArgs")
    public void testConvertToEntity(UserDto userDto) {
        User result = userConverter.convertToEntity(userDto);
        assertNull(result);
    }

    static Stream<Arguments> provideUserTestArgs() {
        User user1 = generateUser("tom");
        User user2 = generateUser("alex");

        return Stream.of(
                Arguments.of(user1, true),
                Arguments.of(user2, false)
        );
    }

    static Stream<Arguments> provideUserDtoTestArgs() {
        UserDto userDto = generateUserDto("tom");

        return Stream.of(
                Arguments.of(userDto)
        );
    }
}
