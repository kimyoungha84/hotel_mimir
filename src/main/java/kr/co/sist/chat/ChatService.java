package kr.co.sist.chat;

import java.util.List;

public interface ChatService {
    int getOrCreateRoom(Long userNum, String chatType);                // 채팅방 생성 또는 기존 방 ID 반환
    List<ChatMessageDTO> loadMessages(Long userNum, String chatType);  // 이전 메시지 로드
    void saveMessage(ChatMessageDTO dto);                              // 메시지 저장
}
