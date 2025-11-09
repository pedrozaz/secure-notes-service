package io.github.pedrozaz.securenotesservice.service;

import io.github.pedrozaz.securenotesservice.dto.UserRegistrationRequest;
import io.github.pedrozaz.securenotesservice.model.PublicKey;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.repository.PublicKeyRepository;
import io.github.pedrozaz.securenotesservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PublicKeyRepository publicKeyRepository;
    private final CryptoService cryptoService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PublicKeyRepository publicKeyRepository, CryptoService cryptoService) {
        this.userRepository = userRepository;
        this.publicKeyRepository = publicKeyRepository;
        this.cryptoService = cryptoService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User registerNewUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalStateException("Username is already in use");
        }

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(newUser);

        KeyPair keyPair = cryptoService.generateDhKeyPair();

        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        PublicKey userPublicKey = new PublicKey();
        userPublicKey.setKeyData(publicKeyBase64);
        userPublicKey.setUser(savedUser);
        publicKeyRepository.save(userPublicKey);

        return savedUser;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public PublicKey getPublicKeyForUser(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + publicId));

        return publicKeyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Public key not found for user: " + user.getUsername()));
    }
}
