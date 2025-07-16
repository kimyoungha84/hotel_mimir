package kr.co.sist.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.member.CustomUserDetails;

// 채팅 컨트롤러: 사용자 채팅 API 제공
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    /**
     * 채팅방 생성 또는 조회
     * chat_type: 0(객실문의), 1(다이닝문의), 2(일반문의)
     */
    @PostMapping("/room")
    // 채팅방 생성 또는 기존 방 조회 (user_num, chat_type 기반)
    public ResponseEntity<?> createOrGetChatRoom(@RequestBody Map<String, String> request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        Integer userNum = userDetails.getUserNum();
        String chatType = request.get("chat_type");
        if (chatType == null || (!chatType.equals("0") && !chatType.equals("1") && !chatType.equals("2"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "올바른 chat_type이 필요합니다."));
        }
        try {
            ChatRoomDTO chatRoom = chatService.getOrCreateChatRoom(userNum, chatType);
            return ResponseEntity.ok(chatRoom);
        } catch (Exception e) {
            e.printStackTrace(); // 반드시 추가!
        	System.out.println(userNum+" / "+chatType);
            return ResponseEntity.status(500).body(Map.of("error", "채팅방 생성 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 채팅방 메시지 조회
     */
    @GetMapping("/messages")
    // 특정 채팅방(room_id) 메시지 전체 조회
    public ResponseEntity<?> getChatMessages(@RequestParam int room_id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        Integer userNum = userDetails.getUserNum();
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
    // 로그인 사용자의 모든 채팅방 목록 조회
    public ResponseEntity<?> getUserChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }
        Integer userNum = userDetails.getUserNum();
        try {
            List<ChatRoomDTO> rooms = chatService.getRoomsByUserId(userNum);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "채팅방 조회 중 오류가 발생했습니다."));
        }
    }
}
