package kr.co.sist.websocket;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.sist.chat.ChatMessageDTO;
import kr.co.sist.chat.ChatMessageMapper;
import kr.co.sist.chat.ChatRoomDTO;
import kr.co.sist.chat.ChatRoomMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private static final Map<WebSocketSession, String> sessionUserMap = new ConcurrentHashMap<>();
    // 채팅방별 세션 관리 (roomId -> userId Set)
    private static final Map<Integer, Set<String>> roomSessions = new ConcurrentHashMap<>();

    @Autowired
    private ChatRoomMapper chatRoomMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        Integer roomId = getRoomId(session);
        if (userId != null && roomId != null) {
            userSessions.put(userId, session);
            sessionUserMap.put(session, userId);
            // 세션 관리
            roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
            System.out.println("접속: " + userId + " (roomId=" + roomId + ")");
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
                chatMsg.setUser_num(Integer.parseInt(sender));
                chatMsg.setStaff_id(room.getStaff_id());
                chatMsg.setIs_from_user("Y");
            } else {
                chatMsg.setUser_num(room.getUser_num());
                chatMsg.setStaff_id(sender);
                chatMsg.setIs_from_user("N");
            }
            chatMsg.setDept_iden(room.getDept_iden());
            chatMsg.setContent(msg);
            chatMsg.setSend_time(new Timestamp(System.currentTimeMillis()));
            chatMsg.setIs_read("0"); // 항상 0으로 저장
            // 채팅방에 2명 이상 접속 중이면 읽음, 아니면 안읽음
            // Set<String> sessions = roomSessions.get(roomId);
            // boolean bothInRoom = sessions != null && sessions.size() > 1;
            // chatMsg.setIs_read(bothInRoom ? "1" : "0");
            // System.out.println("[채팅메시지 저장] sender=" + sender + ", isUser=" + isUser + ", room.getUser_num()=" + room.getUser_num() + ", chatMsg.getUser_num()=" + chatMsg.getUser_num() + ", is_read=" + chatMsg.getIs_read());
            chatMessageMapper.insert(chatMsg);
            // 3. 상대방 세션에 전달
            String targetId;
            if (isUser) {
                targetId = room.getStaff_id();
            } else {
                targetId = String.valueOf(room.getUser_num());
            }
            WebSocketSession targetSession = userSessions.get(targetId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(roomId + ":" + sender + ":" + msg));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = sessionUserMap.remove(session);
        Integer roomId = getRoomId(session);
        if (userId != null) {
            userSessions.remove(userId);
            // 세션 관리
            if (roomId != null && roomSessions.containsKey(roomId)) {
                roomSessions.get(roomId).remove(userId);
                if (roomSessions.get(roomId).isEmpty()) {
                    roomSessions.remove(roomId);
                }
            }
            System.out.println("종료: " + userId + " (roomId=" + roomId + ")");
        }
    }

    private String getUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("userId=")) {
                    String id = param.substring("userId=".length());
                    if (id.startsWith("\"") && id.endsWith("\"")) {
                        id = id.substring(1, id.length() - 1);
                    }
                    return id;
                }
            }
        }
        return null;
    }

    // 쿼리스트링에서 roomId 추출 (예: /chat?userId=123&roomId=456)
    private Integer getRoomId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("roomId=")) {
                    try {
                        return Integer.parseInt(param.substring("roomId=".length()));
                    } catch (Exception e) { return null; }
                }
            }
        }
        return null;
    }

    // Add this method to send read events after marking messages as read
    // public void sendReadEvent(int roomId, String targetId, java.util.List<Integer> messageIds) { ... }
    // public void markMessagesAsReadAndNotify(int roomId, String staffId, String targetId) { ... }
}
