package com.myorg.todo.config.websocket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import com.myorg.todo.chat.ChatUser;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor
                = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message
                    .getHeaders()
                    .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("username");
                if (name instanceof LinkedList) {
                    accessor.setUser(new ChatUser(((LinkedList) name).get(0).toString()));
                } else if (name instanceof ArrayList) {
                    accessor.setUser(new ChatUser(((ArrayList) name).get(0).toString()));
                } else if(name instanceof String) {
                    accessor.setUser(new ChatUser(name.toString()));
                }
            }
        }
        return message;
    }
}