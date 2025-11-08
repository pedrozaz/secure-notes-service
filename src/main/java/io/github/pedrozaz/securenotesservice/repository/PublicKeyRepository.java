package io.github.pedrozaz.securenotesservice.repository;

import io.github.pedrozaz.securenotesservice.model.PublicKey;
import io.github.pedrozaz.securenotesservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublicKeyRepository extends JpaRepository<PublicKey, Long> {
    Optional<PublicKey> findByUser(User user);
    Optional<PublicKey> findByUser_PublicId(UUID userPublicId);
}
