package io.github.pedrozaz.securenotesservice.service;

import io.github.pedrozaz.securenotesservice.dto.CreateNoteRequest;
import io.github.pedrozaz.securenotesservice.dto.NoteDetailsResponse;
import io.github.pedrozaz.securenotesservice.model.Note;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.repository.NoteRepository;
import io.github.pedrozaz.securenotesservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Note createNote(CreateNoteRequest request, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + ownerUsername));

        Note note = new Note();
        note.setEncryptedContent(request.encryptedContent());
        note.setOwner(owner);

        if (request.recipientPublicId() != null) {
            User recipient = userRepository.findByPublicId(request.recipientPublicId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + request.recipientPublicId()));

            if (request.senderEphemeralPublicKey() == null | request.senderEphemeralPublicKey().isBlank()) {
                throw new IllegalArgumentException("Sender Ephemeral Public Key is required to share a note.");
            }

            note.setRecipient(recipient);
            note.setSenderEphemeralPublicKey(request.senderEphemeralPublicKey());
        }

        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<Note> findNotesByOwner(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return owner.getOwnedNotes();
    }

    @Transactional(readOnly = true)
    public List<Note> findNotesReceivedByUser(String username) {
        User recipient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return  recipient.getRecipientNotes();
    }

    @Transactional(readOnly = true)
    public List<NoteDetailsResponse> findReceivedNotesAsDto(String username) {
        User recipient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return recipient.getRecipientNotes().stream()
                .map(this::mapToNoteDetailsResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NoteDetailsResponse> findOwnedNotesAsDto(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return owner.getOwnedNotes().stream()
                .map(this::mapToNoteDetailsResponse)
                .toList();
    }

    private NoteDetailsResponse mapToNoteDetailsResponse(Note note) {
        String recipientUsername = note.getRecipient() != null ? note.getRecipient().getUsername() : null;
        return new NoteDetailsResponse(
                note.getPublicId(),
                note.getEncryptedContent(),
                note.getOwner().getUsername(),
                recipientUsername,
                note.getSenderEphemeralPublicKey()
        );
    }
}
