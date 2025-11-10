package io.github.pedrozaz.securenotesservice.dto;

import java.util.UUID;

public record NoteResponse(
        UUID publicId,
        String ownerUsername,
        String recipientUsername
) {
}
