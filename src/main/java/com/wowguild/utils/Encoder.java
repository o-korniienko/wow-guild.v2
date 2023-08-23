package com.wowguild.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Encoder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encode(String input){
        return passwordEncoder.encode(input);
    }
}
