package io.github.pedrozaz.securenotesservice.controller;

import io.github.pedrozaz.securenotesservice.dto.CreateNoteRequest;
import io.github.pedrozaz.securenotesservice.dto.NoteDetailsResponse;
import io.github.pedrozaz.securenotesservice.dto.NoteResponse;
import io.github.pedrozaz.securenotesservice.dto.UserNotesResponse;
import io.github.pedrozaz.securenotesservice.model.Note;
import io.github.pedrozaz.securenotesservice.service.NoteService;
import io.github.pedrozaz.securenotesservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
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

    @GetMapping
    public ResponseEntity<UserNotesResponse> getUserNotes(Authentication authentication) {
        String username = authentication.getName();

        List<NoteDetailsResponse> ownedNotesDto = noteService.findOwnedNotesAsDto(username);
        List<NoteDetailsResponse> receivedNotesDto = noteService.findReceivedNotesAsDto(username);

        UserNotesResponse response = new UserNotesResponse(ownedNotesDto, receivedNotesDto);
        return ResponseEntity.ok(response);
    }
}
