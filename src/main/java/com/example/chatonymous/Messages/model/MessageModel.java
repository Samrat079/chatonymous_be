package com.example.chatonymous.Messages.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MessageModel {
    @Id
    private String id;

    @NonNull
    private String conversationId, textContent; // createdBy is the username of the maker

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;
}
