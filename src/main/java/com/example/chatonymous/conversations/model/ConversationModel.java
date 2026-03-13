package com.example.chatonymous.conversations.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "conversations")
public class ConversationModel {
    @Id
    private String id;

    private List<String> participants; // participants is the userName
}
