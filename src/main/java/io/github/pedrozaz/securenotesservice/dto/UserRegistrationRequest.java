package io.github.pedrozaz.securenotesservice.dto;

public record UserRegistrationRequest(
        String username,
        String password
) {
}
