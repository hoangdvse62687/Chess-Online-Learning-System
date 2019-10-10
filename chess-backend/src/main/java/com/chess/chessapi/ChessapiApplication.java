package com.chess.chessapi;

import com.chess.chessapi.configs.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ChessapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChessapiApplication.class, args);
    }
}
