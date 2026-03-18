package com.example.chatonymous.contacts;

import com.example.chatonymous.conversations.model.ConversationModel;
import com.example.chatonymous.conversations.service.ConversationsServices;
import com.example.chatonymous.users.model.UserModel;
import com.example.chatonymous.users.services.UsersServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ContactsController {
    private UsersServices usersServices;
    private ConversationsServices convoServices;


    @GetMapping
    public ResponseEntity<?> getOrMakeContacts(
            @RequestParam String participants,
            @AuthenticationPrincipal Jwt jwt
    ) throws InterruptedException {
        String currUserName = jwt.getSubject();
        List<ConversationModel> res = convoServices.searchByUserName(currUserName, participants);

        if (!res.isEmpty()) {
            return ResponseEntity.ok(res);
        }

        TimeUnit.SECONDS.sleep(2);
        List<UserModel> users = usersServices.findByUserName(participants);
        return ResponseEntity.ok(users);
    }
}
