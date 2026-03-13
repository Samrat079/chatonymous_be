package com.example.chatonymous.Messages.controller;

import com.example.chatonymous.Messages.model.MessageModel;
import com.example.chatonymous.Messages.repository.MessagesRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    private MessagesRepository messagesRepository;

    public record MessagePostRecord(@NotNull String conversationId, String textContent, String createdBy) {
    }

    @GetMapping
    public List<MessageModel> getMapping(@RequestParam String conversationId) {
        return messagesRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @PostMapping
    public MessageModel postMapping(@RequestBody MessagePostRecord messagePostRecord) {
        MessageModel messageModel = new MessageModel();
        messageModel.setConversationId(messagePostRecord.conversationId());
        messageModel.setCreatedBy(messagePostRecord.createdBy());
        messageModel.setTextContent(messagePostRecord.textContent());
        return messagesRepository.save(messageModel);
    }

    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id) {
        messagesRepository.deleteById(id);
    }
}
