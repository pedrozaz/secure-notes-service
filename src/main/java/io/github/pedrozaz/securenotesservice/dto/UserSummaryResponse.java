package io.github.pedrozaz.securenotesservice.dto;

import java.util.UUID;

public record UserSummaryResponse(
        UUID publicId,
        String username
) {
}
