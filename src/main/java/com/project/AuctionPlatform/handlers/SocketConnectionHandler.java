package com.project.AuctionPlatform.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// Socket-Connection Configuration class
public class SocketConnectionHandler extends TextWebSocketHandler {

    public final Map<String, List<WebSocketSession>> productSessionsMap = new ConcurrentHashMap<>();

    public SocketConnectionHandler() {
        System.out.println("SocketConnectionHandler instance created: " + this);
    }

    private void addSessionToProduct(String productName, WebSocketSession session) {
        productSessionsMap.computeIfAbsent(productName, k -> Collections.synchronizedList(new ArrayList<>())).add(session);
        System.out.println("Session added to product: " + productName + ". Total sessions for this product: "
                + productSessionsMap.get(productName).size());
    }

    private void removeSessionFromProduct(String productName, WebSocketSession session) {
        List<WebSocketSession> sessions = productSessionsMap.get(productName);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                productSessionsMap.remove(productName);
                System.out.println("All sessions for product " + productName + " removed.");
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // Retrieve product ID from the session's URI or headers
        String productName = getProductIdFromSession(session);
        if (productName != null) {
            addSessionToProduct(productName, session);
            System.out.println("Connection established for product: " + productName);
        } else {

            System.out.println("Invalid connection: No product ID specified.");
        }
    }
    private String getProductIdFromSession(WebSocketSession session) {
        String uri = session.getUri() != null ? session.getUri().toString() : null;

        if (uri != null && uri.contains("productName=")) {
            return uri.substring(uri.indexOf("productName=") + 12).split("&")[0];
        }
        // Alternatively, use headers or other means to get the productId
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        String productId = getProductIdFromSession(session);
        if (productId != null) {
            removeSessionFromProduct(productId, session);
            System.out.println("Connection closed for product: " + productId);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        // Retrieve product ID from the session's URI or headers
        String productId = getProductIdFromSession(session);
        if (productId != null) {
            // Broadcast the message to all sessions for the product
            List<WebSocketSession> sessions = productSessionsMap.get(productId);
            if (sessions != null) {
                for (WebSocketSession webSocketSession : sessions) {
                    if (!session.equals(webSocketSession)) {
                        webSocketSession.sendMessage(message);
                    }
                }
            }
        }
    }
}
