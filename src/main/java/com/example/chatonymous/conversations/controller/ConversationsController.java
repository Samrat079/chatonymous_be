package com.example.chatonymous.conversations.controller;

import com.example.chatonymous.conversations.model.ConversationModel;
import com.example.chatonymous.conversations.model.participantsRecord;
import com.example.chatonymous.conversations.repository.ConversationsRepository;
import com.example.chatonymous.conversations.service.ConversationsServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
public class ConversationsController {

    private ConversationsServices convoServices;

    @GetMapping
    public List<ConversationModel> getMapping(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) List<String> participants
    ) {
        return convoServices.searchByIdOrUserName(id, participants);
    }

    @PostMapping
    public ConversationModel postMapping(@RequestBody participantsRecord participantsRecord) {
        return convoServices.addNewConvo(participantsRecord);
    }

    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id) {
        convoServices.deleteById(id);
    }
}
