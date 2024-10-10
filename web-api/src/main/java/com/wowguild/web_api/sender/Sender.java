package com.wowguild.web_api.sender;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Sender {

    String sendRequest(String url, HttpMethod method, String clientId, String clientSecret);

    String sendRequest(String url, HttpMethod method, String token);

    String sendRequest(String url, Map<String, String> body, HttpMethod method, String token);
}
