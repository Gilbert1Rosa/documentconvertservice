package com.example.documentconvertservice.websocket;

import com.example.documentconvertservice.dto.ProgressDetails;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class ProgressWebSocketHandler extends TextWebSocketHandler {

    private static WebSocketSession MAIN_SESSION = null;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        MAIN_SESSION = session;
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        if (MAIN_SESSION != null) {
            MAIN_SESSION = null;
        }
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) {

    }

    public void sendProgress(double progress) throws IOException {
        if (MAIN_SESSION != null) {
            String message = "{ \"progress\": " + String.format("%.2f", progress) + " }";
            MAIN_SESSION.sendMessage(new TextMessage(message));
        }
    }
}
