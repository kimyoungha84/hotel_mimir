package kr.co.sist.chat;

import java.util.List;

public interface ChatService {
    /**
     * 채팅방을 조회하거나 없으면 생성한다. (관리자 라우팅은 ChatRoomMapper에서 처리)
     */
    ChatRoomDTO getOrCreateChatRoom(int user_num, String chat_type);
    List<ChatMessageDTO> getMessagesByRoomId(int room_id);
    void saveMessage(ChatMessageDTO message);
    /**
     * staff_id로 담당 채팅방 리스트 조회
     */
    List<ChatRoomDTO> getRoomsByStaffId(String staff_id);
    /**
     * user_num으로 사용자의 채팅방 리스트 조회
     */
    List<ChatRoomDTO> getRoomsByUserId(int user_num);
}
