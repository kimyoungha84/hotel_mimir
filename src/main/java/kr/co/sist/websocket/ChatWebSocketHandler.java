package kr.co.sist.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sender = sessionUserMap.get(session);
        String payload = message.getPayload();

        // payload: "room_id:content"
        if (payload.contains(":")) {
            String[] parts = payload.split(":", 2);
            int roomId = Integer.parseInt(parts[0]);
            String msg = parts[1];

            // 1. 채팅방 정보 조회
            ChatRoomDTO room = chatRoomMapper.findByRoomId(roomId);
            if (room == null) return;

            // 2. 메시지 저장
            ChatMessageDTO chatMsg = new ChatMessageDTO();
            chatMsg.setRoom_id(roomId);
            chatMsg.setStaff_id(sender.equals(room.getStaff_id()) ? sender : room.getStaff_id());
            chatMsg.setDept_iden(room.getDept_iden());
            chatMsg.setContent(msg);
            chatMsg.setSend_time(new Timestamp(System.currentTimeMillis()));
            chatMsg.setIs_read("0");
            chatMessageMapper.insert(chatMsg);

            // 3. 상대방 세션에 전달
            String targetId = sender.equals(room.getStaff_id()) ? String.valueOf(room.getUser_num()) : room.getStaff_id();
            WebSocketSession targetSession = userSessions.get(targetId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(sender + ":" + msg));
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
