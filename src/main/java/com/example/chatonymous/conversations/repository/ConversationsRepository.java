package com.example.chatonymous.conversations.repository;

import com.example.chatonymous.conversations.model.ConversationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConversationsRepository extends MongoRepository<ConversationModel, String> {
    List<ConversationModel> findByParticipantsContainsIgnoreCase(String username);
}
