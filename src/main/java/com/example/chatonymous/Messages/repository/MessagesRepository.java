package com.example.chatonymous.Messages.repository;

import com.example.chatonymous.Messages.model.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessagesRepository extends MongoRepository<MessageModel, String> {
    List<MessageModel> findByConversationIdOrderByCreatedAtAsc(String conversationId);
}
