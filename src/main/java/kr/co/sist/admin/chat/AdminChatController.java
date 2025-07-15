package kr.co.sist.admin.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.chat.ChatMessageDTO;
import kr.co.sist.chat.ChatRoomDTO;
import kr.co.sist.chat.ChatService;

@RestController
@RequestMapping("/api/admin/chat")
public class AdminChatController {
    
    @Autowired
    private ChatService chatService;
    
    /**
     * 관리자의 담당 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<?> getAdminChatRooms(@RequestParam String staff_id) {
        try {
            List<ChatRoomDTO> rooms = chatService.getRoomsByStaffId(staff_id);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "채팅방 조회 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 특정 채팅방의 메시지 조회
     */
    @GetMapping("/messages")
    public ResponseEntity<?> getChatMessages(@RequestParam int room_id) {
        try {
            List<ChatMessageDTO> messages = chatService.getMessagesByRoomId(room_id);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "메시지 조회 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 메시지 읽음 처리
     */
    @PostMapping("/read")
    public ResponseEntity<?> markMessagesAsRead(@RequestParam int room_id, @RequestParam String staff_id) {
        try {
            chatService.markMessagesAsRead(room_id, staff_id);
            return ResponseEntity.ok(Map.of("message", "읽음 처리 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "읽음 처리 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 안읽은 메시지 개수 조회
     */
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(@RequestParam int room_id, @RequestParam String staff_id) {
        try {
            int count = chatService.countUnreadByRoomAndStaff(room_id, staff_id);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "안읽은 메시지 조회 중 오류가 발생했습니다."));
        }
    }
} 