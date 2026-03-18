package com.example.chatonymous.conversations.repository;

import com.example.chatonymous.conversations.model.ConversationModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ConversationsRepository extends MongoRepository<ConversationModel, String> {
    boolean existsByParticipants(List<String> participants);
    List<ConversationModel> findByParticipantsContains(List<String> participants);

    @Query("""
    {
      $and: [
        { "participants": ?0 },
        { "participants": { $regex: ?1, $options: "i" } }
      ]
    }
    """)
    List<ConversationModel> searchConversations(String currUser, String search);
}
