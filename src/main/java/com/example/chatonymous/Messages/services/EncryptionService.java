package com.example.chatonymous.Messages.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final TextEncryptor txtEncryptor;

    public EncryptionService(
            @Value("${encryption.key}") String encryptionKey,
            @Value("${encryption.salt}") String salt
    ) {
        this.txtEncryptor = Encryptors.text(encryptionKey, salt);
    }

    public String encrypt(String data) {
        return txtEncryptor.encrypt(data);
    }

    public String decrypt(String data) {
        return txtEncryptor.decrypt(data);
    }
}
