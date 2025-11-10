package io.github.pedrozaz.securenotesservice.controller;

import io.github.pedrozaz.securenotesservice.dto.CreateNoteRequest;
import io.github.pedrozaz.securenotesservice.dto.NoteResponse;
import io.github.pedrozaz.securenotesservice.model.Note;
import io.github.pedrozaz.securenotesservice.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @RequestBody CreateNoteRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        Note createdNote = noteService.createNote(request, username);

        String recipientUsername = null;
        if (createdNote.getRecipient() != null) {
            recipientUsername = createdNote.getRecipient().getUsername();
        }

        NoteResponse noteResponse = new NoteResponse(
                createdNote.getPublicId(),
                createdNote.getOwner().getUsername(),
                recipientUsername
        );

        return new ResponseEntity<>(noteResponse, HttpStatus.CREATED);
    }
}
