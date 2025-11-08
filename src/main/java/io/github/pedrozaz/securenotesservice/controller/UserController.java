package io.github.pedrozaz.securenotesservice.controller;

import io.github.pedrozaz.securenotesservice.dto.UserRegistrationRequest;
import io.github.pedrozaz.securenotesservice.dto.UserResponse;
import io.github.pedrozaz.securenotesservice.model.User;
import io.github.pedrozaz.securenotesservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
