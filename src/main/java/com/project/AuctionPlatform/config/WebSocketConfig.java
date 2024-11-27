package com.project.AuctionPlatform.config;

import com.project.AuctionPlatform.handlers.SocketConnectionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public SocketConnectionHandler socketConnectionHandler() {
        return new SocketConnectionHandler();
    }

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(socketConnectionHandler(),"/bidding")
                .setAllowedOrigins("*");
    }

}
