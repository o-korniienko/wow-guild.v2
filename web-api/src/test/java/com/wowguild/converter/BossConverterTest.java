package com.wowguild.converter;


import com.wowguild.common.converter.BossConverter;
import com.wowguild.common.dto.wow.BossDto;
import com.wowguild.common.dto.wow.ZoneDto;
import com.wowguild.common.entity.wow.rank.Boss;
import com.wowguild.common.entity.wow.rank.Zone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.wowguild.arguments.BossGenerator.generateBoss;
import static com.wowguild.arguments.BossGenerator.generateBossDto;
import static com.wowguild.arguments.ZoneGenerator.generateZone;
import static com.wowguild.arguments.ZoneGenerator.generateZoneDto;
import static org.junit.jupiter.api.Assertions.*;


public class BossConverterTest {


    private BossConverter bossConverter;
    private BossDto bossDtoExpected;
    private ZoneDto zoneDtoExpected;

    @BeforeEach
    void setUp() {
        bossConverter = new BossConverter();
        bossDtoExpected = generateBossDto("boss", 1, 1, 123);
        zoneDtoExpected = generateZoneDto("zone", 1, 1234567890, "exp_name");
    }

    @ParameterizedTest
    @MethodSource("provideBossTestArgs")
    public void testConvertToDto(Boss boss, boolean positiveTest) {
        BossDto bossDtoResult = bossConverter.convertToDto(boss);
        if (positiveTest) {
            assertEquals(bossDtoExpected, bossDtoResult);
        } else {
            assertNotEquals(bossDtoExpected, bossDtoResult);
        }
    }

    @ParameterizedTest
    @MethodSource("provideBossDtoTestArgs")
    public void testConvertToEntity(BossDto bossDto) {
        Boss result = bossConverter.convertToEntity(bossDto);
        assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("provideZoneTestArgs")
    public void testConvertToZoneDto(Zone zone, boolean positiveTest) {
        ZoneDto zoneDtoResult = bossConverter.convertToZoneDto(zone);
        if (positiveTest) {
            assertEquals(zoneDtoExpected, zoneDtoResult);
        } else {
            assertNotEquals(zoneDtoExpected, zoneDtoResult);
        }
    }

    static Stream<Arguments> provideBossTestArgs() {
        Boss boss = generateBoss("boss", 1, 1, 123);
        Boss boss1 = generateBoss("boss1", 1, 1, 123);
        Boss boss2 = generateBoss("boss", 2, 1, 123);
        Boss boss3 = generateBoss("boss", 1, 3, 123);
        Boss boss4 = generateBoss("boss", 1, 1, 124);


        return Stream.of(
                Arguments.of(boss, true),
                Arguments.of(boss1, false),
                Arguments.of(boss2, false),
                Arguments.of(boss3, false),
                Arguments.of(boss4, false)
        );
    }

    static Stream<Arguments> provideBossDtoTestArgs() {
        BossDto bossDto = generateBossDto("boss", 1, 1, 123);

        return Stream.of(
                Arguments.of(bossDto)
        );
    }

    static Stream<Arguments> provideZoneTestArgs() {
        Zone zone = generateZone("zone", 1, 1234567890, "exp_name");
        Zone zone1 = generateZone("zone1", 1, 1234567890, "exp_name");
        Zone zone2 = generateZone("zone", 2, 1234567890, "exp_name");
        Zone zone3 = generateZone("zone", 1, 1234567891, "exp_name");
        Zone zone4 = generateZone("zone", 1, 1234567890, "exp_name2");

        return Stream.of(
                Arguments.of(zone, true),
                Arguments.of(zone1, false),
                Arguments.of(zone2, false),
                Arguments.of(zone3, false),
                Arguments.of(zone4, false)
        );
    }

}
