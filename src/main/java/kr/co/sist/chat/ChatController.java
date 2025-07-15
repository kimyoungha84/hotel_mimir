package kr.co.sist.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.member.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    /**
     * JWT 토큰에서 사용자 정보 추출
     */
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserNum();
        }
        return null;
    }
    
    /**
     * 채팅방 생성 또는 조회
     * chat_type: 0(객실문의), 1(다이닝문의), 2(일반문의)
     */
    @PostMapping("/room")
    public ResponseEntity<?> createOrGetChatRoom(@RequestBody Map<String, String> request) {
        Integer userNum = getCurrentUserId();
        if (userNum == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        
        String chatType = request.get("chat_type");
        if (chatType == null || (!chatType.equals("0") && !chatType.equals("1") && !chatType.equals("2"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "올바른 chat_type이 필요합니다."));
        }
        
        try {
            ChatRoomDTO chatRoom = chatService.getOrCreateChatRoom(userNum, chatType);
            return ResponseEntity.ok(chatRoom);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "채팅방 생성 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 채팅방 메시지 조회
     */
    @GetMapping("/messages")
    public ResponseEntity<?> getChatMessages(@RequestParam int room_id) {
        Integer userNum = getCurrentUserId();
        if (userNum == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        
        try {
            List<ChatMessageDTO> messages = chatService.getMessagesByRoomId(room_id);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "메시지 조회 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 사용자의 모든 채팅방 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<?> getUserChatRooms() {
        Integer userNum = getCurrentUserId();
        if (userNum == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        
        try {
            // chat_type별로 채팅방 조회 (0, 1, 2)
            List<ChatRoomDTO> rooms = chatService.getRoomsByUserId(userNum);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "채팅방 조회 중 오류가 발생했습니다."));
        }
    }
}
