package com.example.chatonymous.users.controller;

import com.example.chatonymous.users.model.JwtResponceRecord;
import com.example.chatonymous.users.model.UserNamePasswordRecord;
import com.example.chatonymous.users.services.TokenService;
import com.example.chatonymous.users.model.UserModel;
import com.example.chatonymous.users.services.UsersServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UsersController {
    private UsersServices usersServices;
    private AuthenticationManager authManager;
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getMapping(
            @RequestParam(required = false) String userName
    ) {
        return usersServices.findByOrUserName(userName);
    }

    @GetMapping("/current_user")
    public ResponseEntity<UserModel> currentUser() {
        return usersServices.getCurrentUser();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserNamePasswordRecord userRecord) {
        return usersServices.signup(userRecord);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserNamePasswordRecord userRecord) {
        Authentication auth;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRecord.userName(), userRecord.passWord())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }

        String token = tokenService.generate(auth.getName());
        return ResponseEntity.ok(new JwtResponceRecord(token));
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<?> deleteMapping(@PathVariable String userName) {
        return usersServices.delete(userName);
    }
}
