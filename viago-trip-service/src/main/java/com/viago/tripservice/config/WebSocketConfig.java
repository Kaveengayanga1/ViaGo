package com.viago.tripservice.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Service
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Frontends SUBSCRIBE to these
        config.enableSimpleBroker("/topic", "/queue");
        // Frontends SEND messages to these
        config.setApplicationDestinationPrefixes("/app");
        // User specific notifications
        config.setUserDestinationPrefix("/user");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // React Frontend Connects here
        registry.addEndpoint("/ws-ride").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws-ride").setAllowedOriginPatterns("*");
    }
}
