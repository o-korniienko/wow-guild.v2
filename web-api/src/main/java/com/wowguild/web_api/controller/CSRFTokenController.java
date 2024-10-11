package com.wowguild.web_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSRFTokenController {
    @RequestMapping("/csrf")
    public ResponseEntity<?> csrf(CsrfToken token) {
        return ResponseEntity.ok(token);
    }
}
