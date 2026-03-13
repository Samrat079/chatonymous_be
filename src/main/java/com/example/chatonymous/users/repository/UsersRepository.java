package com.example.chatonymous.users.repository;

import com.example.chatonymous.users.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByUserNameIgnoreCase(String username);
    List<UserModel> findByUserNameContains(String username);

    boolean existsByUserNameIgnoreCase(String username);

    void deleteByUserNameIgnoreCase(String username);
}
