package com.example.chatonymous.conversations.service;

import com.example.chatonymous.conversations.model.ConversationModel;
import com.example.chatonymous.conversations.model.participantsRecord;
import com.example.chatonymous.conversations.repository.ConversationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationsServices {
    private ConversationsRepository convoRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ConversationModel> searchByIdOrUserName(String id, List<String> participants) {
        Query query = new Query();
        if (id != null) {
            query.addCriteria(Criteria.where("id").is(id));
        }
        if (participants != null && !participants.isEmpty()) {
            query.addCriteria(Criteria.where("participants").in(participants));
        }
        return mongoTemplate.find(query, ConversationModel.class);
    }

    public ConversationModel addNewConvo(participantsRecord participantsRecord) {
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setParticipants(participantsRecord.participants());
        return convoRepo.save(conversationModel);
    }

    public void deleteById(String id) {
        convoRepo.deleteById(id);
    }
}
