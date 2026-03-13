package com.example.chatonymous;

//import com.example.chatonymous.users.auth.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
@EnableConfigurationProperties()
public class ChatonymousApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatonymousApplication.class, args);
    }

}
