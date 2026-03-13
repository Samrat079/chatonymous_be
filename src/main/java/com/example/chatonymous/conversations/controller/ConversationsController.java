package com.example.chatonymous.conversations.controller;

import com.example.chatonymous.conversations.model.ConversationModel;
import com.example.chatonymous.conversations.repository.ConversationsRepository;
import com.example.chatonymous.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
public class ConversationsController {

    private ConversationsRepository conversationsRepository;

    public record participantsRecord(List<String> participants){};

    @GetMapping
    public List<ConversationModel> getMapping(@RequestParam String userName) {
        return conversationsRepository.findByParticipantsContainsIgnoreCase(userName);
    }

    @PostMapping
    public ConversationModel postMapping(@RequestBody participantsRecord participantsRecord){
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setParticipants(participantsRecord.participants());
        return conversationsRepository.save(conversationModel);
    }

    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id){
        conversationsRepository.deleteById(id);
    }
}
