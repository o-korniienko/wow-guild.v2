package com.wowguild;

import com.wowguild.config.WowGuildConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {WebApi.class})
@ActiveProfiles("test")
public class WebApiTest {

    @Autowired
    private WowGuildConfig wowGuildConfig;

    @Test
    void testGsonBean(){
        assertNotNull(wowGuildConfig.getGson());
    }

    @Test
    void testRestTemplateBean(){
        assertNotNull(wowGuildConfig.getRest());
    }


}
