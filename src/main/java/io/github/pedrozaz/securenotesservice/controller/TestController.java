package io.github.pedrozaz.securenotesservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(String.format("Hello, %s! Your token is valid.", username));
    }
}
