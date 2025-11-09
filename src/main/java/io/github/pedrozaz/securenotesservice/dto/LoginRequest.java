package io.github.pedrozaz.securenotesservice.dto;

public record LoginRequest(
        String username,
        String password
) {
}
