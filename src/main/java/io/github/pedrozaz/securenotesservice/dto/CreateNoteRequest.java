package io.github.pedrozaz.securenotesservice.dto;

public record CreateNoteRequest(
        String encryptedContent
) {
}
