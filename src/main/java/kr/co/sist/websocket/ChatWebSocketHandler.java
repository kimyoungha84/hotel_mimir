package kr.co.sist.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import kr.co.sist.chat.ChatRoomMapper;
import kr.co.sist.chat.ChatMessageMapper;
import kr.co.sist.chat.ChatRoomDTO;
import kr.co.sist.chat.ChatMessageDTO;
import java.sql.Timestamp;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private static final Map<WebSocketSession, String> sessionUserMap = new ConcurrentHashMap<>();

    @Autowired
    private ChatRoomMapper chatRoomMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        if (userId != null) {
            userSessions.put(userId, session);
            sessionUserMap.put(session, userId);
            System.out.println("접속: " + userId);
            System.out.println("[DEBUG] userSessions: " + userSessions.keySet());
            System.out.println("[DEBUG] sessionUserMap: " + sessionUserMap.values());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sender = sessionUserMap.get(session);
        String payload = message.getPayload();
        System.out.println("[DEBUG] handleTextMessage sender=" + sender + ", payload=" + payload);
        System.out.println("[DEBUG] userSessions: " + userSessions.keySet());

        // payload: "room_id:content"
        if (payload.contains(":")) {
            String[] parts = payload.split(":", 2);
            int roomId = Integer.parseInt(parts[0]);
            String msg = parts[1];

            // 1. 채팅방 정보 조회
            ChatRoomDTO room = chatRoomMapper.findByRoomId(roomId);
            if (room == null) return;

            // 2. 메시지 저장 (user/admin 구분)
            ChatMessageDTO chatMsg = new ChatMessageDTO();
            chatMsg.setRoom_id(roomId);
            
            // sender가 숫자인지 확인하여 사용자/관리자 구분
            boolean isUser = false;
            try {
                Integer.parseInt(sender);
                isUser = true;
            } catch (NumberFormatException e) {
                isUser = false;
            }
            
            if (isUser) {
                // 사용자가 보낸 메시지
                chatMsg.setUser_num(Integer.parseInt(sender)); // sender(사용자 번호)
                chatMsg.setStaff_id(room.getStaff_id());       // 방의 staff_id
                chatMsg.setIs_from_user("Y"); // 사용자 메시지
            } else {
                // 관리자가 보낸 메시지
                chatMsg.setUser_num(room.getUser_num());       // 방의 user_num
                chatMsg.setStaff_id(sender);                   // sender(관리자 ID)
                chatMsg.setIs_from_user("N"); // 관리자 메시지
            }
            
            chatMsg.setDept_iden(room.getDept_iden());
            chatMsg.setContent(msg);
            chatMsg.setSend_time(new Timestamp(System.currentTimeMillis()));
            chatMsg.setIs_read("0");

            // 로그 추가
            System.out.println("[채팅메시지 저장] sender=" + sender + ", isUser=" + isUser + ", room.getUser_num()=" + room.getUser_num() + ", chatMsg.getUser_num()=" + chatMsg.getUser_num());

            chatMessageMapper.insert(chatMsg);

            // 3. 상대방 세션에 전달
            String targetId;
            if (isUser) {
                // 사용자가 보낸 메시지 → 관리자에게 전달
                targetId = room.getStaff_id();
            } else {
                // 관리자가 보낸 메시지 → 사용자에게 전달
                targetId = String.valueOf(room.getUser_num());
            }
            
            WebSocketSession targetSession = userSessions.get(targetId);
            if (targetSession != null && targetSession.isOpen()) {
                // 메시지 포맷: roomId:sender:msg
                targetSession.sendMessage(new TextMessage(roomId + ":" + sender + ":" + msg));
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
            String id = query.split("=")[1];
            // 쌍따옴표가 있으면 제거
            if (id.startsWith("\"") && id.endsWith("\"")) {
                id = id.substring(1, id.length() - 1);
            }
            return id;
        }
        return null;
    }
}
