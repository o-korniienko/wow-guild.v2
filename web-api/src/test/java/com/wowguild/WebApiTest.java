package com.wowguild;

import com.wowguild.web_api.WebApi;
import com.wowguild.web_api.config.WebApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {WebApi.class})
@ActiveProfiles("test")
public class WebApiTest {

    @Autowired
    private WebApiConfig webApiConfig;

    @Test
    void testGsonBean(){
        assertNotNull(webApiConfig.getGson());
    }

    @Test
    void testRestTemplateBean(){
        assertNotNull(webApiConfig.getRest());
    }


}
