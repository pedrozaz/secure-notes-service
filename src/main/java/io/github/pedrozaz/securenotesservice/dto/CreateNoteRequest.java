package io.github.pedrozaz.securenotesservice.dto;

import java.util.UUID;

public record CreateNoteRequest(
        String encryptedContent,
        UUID recipientPublicId,
        String senderEphemeralPublicKey
) {
}
