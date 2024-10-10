package com.wowguild.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Encoder {

    private final PasswordEncoder passwordEncoder;

    public String encode(String input) {
        return passwordEncoder.encode(input);
    }
}
