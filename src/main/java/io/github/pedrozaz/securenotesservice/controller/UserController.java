package io.github.pedrozaz.securenotesservice.controller;

import io.github.pedrozaz.securenotesservice.dto.PublicKeyResponse;
import io.github.pedrozaz.securenotesservice.dto.UserRegistrationRequest;
import io.github.pedrozaz.securenotesservice.dto.UserResponse;
import io.github.pedrozaz.securenotesservice.dto.UserSummaryResponse;
import io.github.pedrozaz.securenotesservice.model.PublicKey;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        User registeredUser = userService.registerNewUser(request);

        UserResponse response = new UserResponse(
                registeredUser.getPublicId(),
                registeredUser.getUsername()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserSummaryResponse> response = users.stream()
                .map(user -> new UserSummaryResponse(user.getPublicId(), user.getUsername()))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publicId}/key")
    public ResponseEntity<PublicKeyResponse> getUserPublicKey(@PathVariable UUID publicId) {
        PublicKey publicKey = userService.getPublicKeyForUser(publicId);
        PublicKeyResponse response = new PublicKeyResponse(publicKey.getKeyData());
        
        return ResponseEntity.ok(response);
    }
}
