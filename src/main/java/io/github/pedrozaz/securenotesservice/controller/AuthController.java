package io.github.pedrozaz.securenotesservice.controller;

import io.github.pedrozaz.securenotesservice.dto.LoginRequest;
import io.github.pedrozaz.securenotesservice.dto.LoginResponse;
import io.github.pedrozaz.securenotesservice.service.JwtService;
import io.github.pedrozaz.securenotesservice.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
