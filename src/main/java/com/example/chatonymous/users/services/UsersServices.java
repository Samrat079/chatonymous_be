package com.example.chatonymous.users.services;

import com.example.chatonymous.users.model.UserModel;
import com.example.chatonymous.users.model.UserNamePasswordRecord;
import com.example.chatonymous.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UsersServices implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private MongoTemplate mongoTemplate;

    public ResponseEntity<List<UserModel>> findByOrUserName(String userName) {
        Query query = new Query();
        String currUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Criteria criteria = Criteria.where("userName").ne(currUser);

        if (userName != null && !userName.isBlank()) {
            Pattern pattern = Pattern.compile(".*" + Pattern.quote(userName) + ".*", Pattern.CASE_INSENSITIVE);
            criteria.regex(pattern);
        }

        query.addCriteria(criteria);
        return ResponseEntity.ok(mongoTemplate.find(query, UserModel.class));
    }

    public List<UserModel> findByUserName(String userName) {
        return usersRepository.findByUserNameContaining(userName);
    }

    public ResponseEntity<UserModel> getCurrentUser() {
        String currUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel userByName = usersRepository.findByUserNameIgnoreCase(currUser).orElse(null);
        return ResponseEntity.ok(userByName);
    }


    public ResponseEntity<?> signup(UserNamePasswordRecord userRecord) {
        if (usersRepository.existsByUserNameIgnoreCase(userRecord.userName())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        UserModel newUser = new UserModel();
        newUser.setUserName(userRecord.userName());
        newUser.setPassWord(Objects.requireNonNull(passwordEncoder.encode(userRecord.passWord())));
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
