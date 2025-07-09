package kr.co.sist.websocket;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.sist.chat.ChatMessageDTO;
import kr.co.sist.chat.ChatService;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private static final Map<WebSocketSession, String> sessionUserMap = new ConcurrentHashMap<>();

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        userSessions.put(userId, session);
        sessionUserMap.put(session, userId);
        System.out.println("✅ WebSocket 연결됨: userId = " + userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userId = getUserId(session);
        String msg = message.getPayload();

        System.out.println("✅ 메시지 수신: " + userId + " → " + msg);

        // userNum 추출
        long userNum = Long.parseLong(userId.replaceAll("\\D", ""));

        // DB 저장용 DTO 생성
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setContent(msg);
        dto.setSend_time(new Timestamp(System.currentTimeMillis()));
        dto.setIs_read("N");

        // 채팅방 ID 가져오기
        String chatType = getChatType(session); // 0: room, 1: dining, 2: 일반문의
        int roomId = chatService.getOrCreateRoom(userNum, chatType);
        dto.setRoom_id(roomId);

        // 저장
        chatService.saveMessage(dto);

        // 관리자에게 전달 (테스트용 하드코딩)
        String adminId = "admin"; // 나중엔 관리자 선택 로직으로 교체 예정
        WebSocketSession adminSession = userSessions.get(adminId);
        if (adminSession != null && adminSession.isOpen()) {
            adminSession.sendMessage(new TextMessage(userId + ":" + msg));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = sessionUserMap.get(session);
        userSessions.remove(userId);
        sessionUserMap.remove(session);
        System.out.println("✅ WebSocket 연결 종료: " + userId);
    }

    private String getUserId(WebSocketSession session) {
        // ws://localhost:8080/chat?userId=10001&chatType=0
        String query = session.getUri().getQuery();
        for (String param : query.split("&")) {
            if (param.startsWith("userId=")) {
                return param.split("=")[1];
            }
        }
        return "10001";
    }

    private String getChatType(WebSocketSession session) {
        String query = session.getUri().getQuery();
        for (String param : query.split("&")) {
            if (param.startsWith("chatType=")) {
                return param.split("=")[1];
            }
        }
        return "2"; // 기본 일반문의
    }
}
