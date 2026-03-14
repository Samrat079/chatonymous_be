package com.example.chatonymous.users.services;

import com.example.chatonymous.users.model.UserModel;
import com.example.chatonymous.users.model.UserNamePasswordRecord;
import com.example.chatonymous.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServices implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<List<UserModel>> findByOrUserName(String userName) {
        if (userName == null) {
            return ResponseEntity.ok(usersRepository.findAll());
        }

        return ResponseEntity.ok(usersRepository.findByUserNameContains(userName));
    }

    public ResponseEntity<UserModel> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String currUser = jwt.getSubject();
        UserModel userByName = usersRepository.findByUserNameIgnoreCase(currUser).orElse(null);
        return ResponseEntity.ok(userByName);
    }


    public ResponseEntity<?> signup(UserNamePasswordRecord userRecord) {
        if (usersRepository.existsByUserNameIgnoreCase(userRecord.userName())){
            return ResponseEntity.badRequest().body("User already exists");
        }

        UserModel newUser = new UserModel();
        newUser.setUserName(userRecord.userName());
        newUser.setPassWord(passwordEncoder.encode(userRecord.passWord()));
        return ResponseEntity.ok(usersRepository.save(newUser));
    }

    public ResponseEntity<?> delete(String userName) {
        if (!usersRepository.existsByUserNameIgnoreCase(userName)) {
            return ResponseEntity.notFound().build();
        }
        usersRepository.deleteByUserNameIgnoreCase(userName);
        return ResponseEntity.ok().build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = usersRepository.findByUserNameIgnoreCase(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        return User
                .withUsername(user.get().getUserName())
                .password(user.get().getPassWord())
                .build();
    }
}
