package kr.co.sist.chat;

import java.util.List;

public interface ChatService {
    /**
     * 채팅방을 조회하거나 없으면 생성한다. (관리자 라우팅은 ChatRoomMapper에서 처리)
     */
    ChatRoomDTO getOrCreateChatRoom(int user_num, String chat_type);
    List<ChatMessageDTO> getMessagesByRoomId(int room_id);
    void saveMessage(ChatMessageDTO message);
    void markMessagesAsRead(int room_id, String staff_id);
    /**
     * staff_id로 담당 채팅방 리스트 조회
     */
    List<ChatRoomDTO> getRoomsByStaffId(String staff_id);
    /**
     * user_num으로 사용자의 채팅방 리스트 조회
     */
    List<ChatRoomDTO> getRoomsByUserId(int user_num);
    /**
     * 안읽은 메시지 개수 (특정 방, 특정 staff_id 기준)
     */
    int countUnreadByRoomAndStaff(int room_id, String staff_id);
}
