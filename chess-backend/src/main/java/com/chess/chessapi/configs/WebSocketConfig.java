package com.chess.chessapi.configs;

import com.chess.chessapi.sockets.ChessGameSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chessGameSocket(), "/chess-socket/{game-history-id}").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chessGameSocket() {
        return new ChessGameSocketHandler();
    }
}
