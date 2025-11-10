package io.github.pedrozaz.securenotesservice.dto;

import java.util.UUID;

public record NoteDetailsResponse(
        UUID publicId,
        String encryptedContent,
        String ownerUsername,
        String recipientUsername,
        String senderEphemeralPublicKey
) {
}
