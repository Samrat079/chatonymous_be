package com.example.chatonymous.Messages.controller;

import com.example.chatonymous.Messages.model.MessageModel;
import com.example.chatonymous.Messages.repository.MessagesRepository;
import com.example.chatonymous.Messages.services.EncryptionService;
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
    private EncryptionService encryptionService;

    public record MessagePostRecord(@NotNull String conversationId, String textContent) {
    }

    @GetMapping
    public List<MessageModel> getMapping(@RequestParam String conversationId) {
        List<MessageModel> msg = messagesRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        for  (MessageModel m : msg) {
            m.setTextContent(encryptionService.decrypt(m.getTextContent()));
        }
        return msg;
    }

    @PostMapping
    public MessageModel postMapping(@RequestBody MessagePostRecord messagePostRecord) {
        MessageModel messageModel = new MessageModel();
        messageModel.setConversationId(messagePostRecord.conversationId());

        String hashText = encryptionService.encrypt(messagePostRecord.textContent());

        messageModel.setTextContent(hashText);
        messageModel.setEncryptedContent(hashText);

        return messagesRepository.save(messageModel);
    }

    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id) {
        messagesRepository.deleteById(id);
    }
}
