package io.github.pedrozaz.securenotesservice.service;

import io.github.pedrozaz.securenotesservice.dto.UserRegistrationRequest;
import io.github.pedrozaz.securenotesservice.model.PublicKey;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.repository.PublicKeyRepository;
import io.github.pedrozaz.securenotesservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Base64;

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
}
