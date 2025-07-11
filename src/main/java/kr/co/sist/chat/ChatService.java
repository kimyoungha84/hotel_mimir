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
}
