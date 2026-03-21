package com.example.chatonymous.conversations.service;

import com.example.chatonymous.conversations.model.ConversationModel;
import com.example.chatonymous.conversations.model.participantsRecord;
import com.example.chatonymous.conversations.repository.ConversationsRepository;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ConversationsServices {
    @Autowired
    private ConversationsRepository convoRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ConversationModel> searchByIdOrUserName(
            String id, List<String> participants
    ) {
        Query query = new Query();
        if (id != null) {
            query.addCriteria(Criteria.where("id").is(id));
        }
        if (participants != null && !participants.isEmpty()) {
            query.addCriteria(Criteria.where("participants").all(participants));
        }
        return mongoTemplate.find(query, ConversationModel.class);
    }

    public List<ConversationModel> searchByUserName(String currUser, String participant) {
        return convoRepo.searchConversations(currUser, participant);
    }

    public ResponseEntity<?> addNewConvo(participantsRecord participantsRecord) {
        // Without this the order becomes a issue
        List<String> participants = participantsRecord.participants();
        Collections.sort(participants);

        if (convoRepo.existsByParticipants(participantsRecord.participants())) {
            return ResponseEntity.badRequest().body("message: Conversation already exists");
        }

        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setParticipants(participantsRecord.participants());
        return ResponseEntity.ok(convoRepo.save(conversationModel));
    }

    public void deleteById(String id) {
        convoRepo.deleteById(id);
    }
}
