package kr.co.sist.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatTestController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/room")
    public ChatRoomDTO getOrCreateRoom(@RequestParam int user_num, @RequestParam String chat_type) {
        return chatService.getOrCreateChatRoom(user_num, chat_type);
    }

    @PostMapping("/message")
    public void sendMessage(@RequestBody ChatMessageDTO message) {
        chatService.saveMessage(message);
    }

    @GetMapping("/messages")
    public List<ChatMessageDTO> getMessages(@RequestParam int room_id) {
        return chatService.getMessagesByRoomId(room_id);
    }

    @GetMapping("/rooms")
    public List<ChatRoomDTO> getRooms(@RequestParam String staff_id) {
        return chatService.getRoomsByStaffId(staff_id);
    }

    /**
     * 안읽은 메시지 개수 반환 (특정 방, 특정 staff_id 기준)
     */
    @GetMapping("/unread-count")
    public int getUnreadCount(@RequestParam int room_id, @RequestParam String staff_id) {
        return chatService.countUnreadByRoomAndStaff(room_id, staff_id);
    }
} 