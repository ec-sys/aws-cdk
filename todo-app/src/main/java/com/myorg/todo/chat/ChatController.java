package com.myorg.todo.chat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/chat-app")
public class ChatController implements ActiveUserChangeListener {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

    @PostConstruct
    private void init() {
        activeUserManager.registerListener(this);
    }

    @PreDestroy
    private void destroy() {
        activeUserManager.removeListener(this);
    }

    @GetMapping
    public String getChatWithSockJs(Model model, @AuthenticationPrincipal OidcUser user) {
        return "sockjs-message";
    }

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) throws Exception {
        String sender = sha.getUser().getName();
        ChatMessage message = new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), chatMessage.getRecipient());
        if (!sender.equals(chatMessage.getRecipient())) {
            webSocket.convertAndSendToUser(sender, "/queue/messages", message);
        }

        webSocket.convertAndSendToUser(chatMessage.getRecipient(), "/queue/messages", message);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}