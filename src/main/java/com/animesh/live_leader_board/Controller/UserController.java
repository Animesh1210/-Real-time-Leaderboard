package com.animesh.live_leader_board.Controller;

import com.animesh.live_leader_board.DTO.UserRegistrationRequest;
import com.animesh.live_leader_board.Model.User;
import com.animesh.live_leader_board.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerUser(
            @Valid @RequestBody Mono<UserRegistrationRequest> request
    ) {
        return request
                .flatMap(req -> {
                    //move this logic to service,
                    //implement log4j !!
                    return userRepository.existsByUsername(req.getUsername())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.just(ResponseEntity
                                            .badRequest()
                                            .body("Username already exists"));
                                }
                                // Create a new user
                                User user = new User();
                                user.setUsername(req.getUsername());
                                user.setPassword(passwordEncoder.encode(req.getPassword()));
                                user.setEmail(req.getEmail());

                                return userRepository.save(user)
                                        .thenReturn(ResponseEntity.ok("User registered successfully"));
                            });
                });
    }
}