package kr.co.sist.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/test/chat")
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
} 