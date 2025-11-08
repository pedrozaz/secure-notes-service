package io.github.pedrozaz.securenotesservice.repository;

import io.github.pedrozaz.securenotesservice.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByPublicId(UUID publicId);
}
