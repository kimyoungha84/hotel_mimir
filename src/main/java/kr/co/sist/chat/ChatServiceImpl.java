package kr.co.sist.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.sist.administrator.AdministratorMapper;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired(required = false)
    private ChatRoomMapper chatRoomMapper; // 가정: ChatRoomMapper가 존재
    @Autowired(required = false)
    private ChatMessageMapper chatMessageMapper; // 가정: ChatMessageMapper가 존재

    @Override
    public ChatRoomDTO getOrCreateChatRoom(int user_num, String chat_type) {
        // staff_id를 무조건 mimir_267801로 고정
        String staff_id = "mimir_267801";
        ChatRoomDTO room = chatRoomMapper.findByUserAndStaffAndType(user_num, staff_id, chat_type);
        if (room == null) {
            room = new ChatRoomDTO();
            room.setUser_num(user_num);
            room.setStaff_id(staff_id);
            room.setDept_iden("room");
            room.setChat_type(chat_type);
            chatRoomMapper.insert(room);
        }
        return room;
    }

    @Override
    public List<ChatMessageDTO> getMessagesByRoomId(int room_id) {
        return chatMessageMapper.findByRoomId(room_id);
    }

    @Override
    public void saveMessage(ChatMessageDTO message) {
        chatMessageMapper.insert(message);
    }

    @Override
    public void markMessagesAsRead(int room_id, String staff_id) {
        chatMessageMapper.markAsRead(room_id, staff_id);
    }

    @Override
    public List<ChatRoomDTO> getRoomsByStaffId(String staff_id) {
        return chatRoomMapper.findRoomsByStaffId(staff_id);
    }

    @Override
    public int countUnreadByRoomAndStaff(int room_id, String staff_id) {
        // 특정 방, 특정 staff_id 기준 안읽은 메시지 개수 반환
        return chatMessageMapper.countUnreadByRoomAndStaff(room_id, staff_id);
    }
}
