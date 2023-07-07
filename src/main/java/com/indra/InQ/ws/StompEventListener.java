package com.indra.InQ.ws;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class StompEventListener {

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        // Print connection details
        System.out.println("New client connected - Session ID: " + sessionId + ", Destination: " + destination);

        // Your logic for handling a new client connection
        // ...
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        // Print disconnection details
        System.out.println("Client disconnected - Session ID: " + sessionId + ", Destination: " + destination);

        // Your logic for handling a client disconnection
        // ...
    }
    @EventListener
    public void handleSessionDisconnect(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String subscriptionId = accessor.getSubscriptionId();

        // Print Subscribed details
        System.out.println("Client Subscribed - Session ID: " + sessionId + ", subscriptionId: " + subscriptionId);
    }
}