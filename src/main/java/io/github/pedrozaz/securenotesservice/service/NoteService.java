package io.github.pedrozaz.securenotesservice.service;

import io.github.pedrozaz.securenotesservice.dto.CreateNoteRequest;
import io.github.pedrozaz.securenotesservice.model.Note;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.repository.NoteRepository;
import io.github.pedrozaz.securenotesservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        return noteRepository.save(note);
    }
}
