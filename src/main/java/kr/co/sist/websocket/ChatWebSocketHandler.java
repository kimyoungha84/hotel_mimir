package kr.co.sist.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private static final Map<WebSocketSession, String> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        if (userId != null) {
            userSessions.put(userId, session);
            sessionUserMap.put(session, userId);
            System.out.println("접속: " + userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sender = sessionUserMap.get(session);
        String payload = message.getPayload();

        // 관리자 → "user123:메시지", 사용자 → "메시지"
        if (payload.contains(":")) {
            String[] parts = payload.split(":", 2);
            String toUser = parts[0];
            String msg = parts[1];

            WebSocketSession targetSession = userSessions.get(toUser);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(sender + ":" + msg));
            }
        } else {
            // 사용자 메시지는 관리자에게만 전달
            WebSocketSession adminSession = userSessions.get("admin");
            if (adminSession != null && adminSession.isOpen()) {
                adminSession.sendMessage(new TextMessage(sender + ":" + payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = sessionUserMap.remove(session);
        if (userId != null) {
            userSessions.remove(userId);
            System.out.println("종료: " + userId);
        }
    }

    private String getUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("userId=")) {
            return query.split("=")[1];
        }
        return null;
    }
}
