package com.indra.InQ.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        Stomp connection endpoint
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        This application receive request to this prefix (controller endpoint prefix)
        registry.setApplicationDestinationPrefixes("/appWs");
//        This app send response to these prefix
        registry.enableSimpleBroker("/public", "/entity","/user","/private");
        registry.setUserDestinationPrefix("/private");
    }
}