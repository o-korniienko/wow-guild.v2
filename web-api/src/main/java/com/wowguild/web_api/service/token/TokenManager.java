package com.wowguild.web_api.service.token;


import org.springframework.stereotype.Service;

@Service
public interface TokenManager {

    String getTokenByTag(String tag);
}
