package io.github.pedrozaz.securenotesservice.dto;

import java.util.UUID;

public record UserResponse(
        UUID publicId,
        String username
) {
}
