package io.github.pedrozaz.securenotesservice.dto;

import java.util.List;

public record UserNotesResponse(
        List<NoteDetailsResponse> ownedNotes,
        List<NoteDetailsResponse> receivedNotes
) {
}
