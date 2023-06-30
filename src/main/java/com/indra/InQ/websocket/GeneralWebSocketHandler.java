package com.indra.InQ.websocket;

import org.springframework.web.socket.*;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

public class GeneralWebSocketHandler implements WebSocketHandler {
    private final Map<String, Session> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String text = message.getPayload().toString();
        for (Session s : sessions.values()) {
//            s.sendMessage(new TextMessage(text));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Transport error: " + exception.getMessage());
        sessions.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed: " + status.getCode());
        sessions.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
