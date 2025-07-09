package kr.co.sist.chat;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMapper {

    // 1. 채팅방 조회 (사용자 + 유형)
    Integer findRoomIdByUserAndType(@Param("userNum") Long userNum, @Param("chatType") String chatType);

    // 2. 채팅방 생성
    void insertChatRoom(ChatRoomDTO dto);

    // 3. 채팅방 정보 조회
    ChatRoomDTO getRoomInfo(@Param("roomId") int roomId);

    // 4. 채팅 메시지 조회
    List<ChatMessageDTO> findMessagesByRoomId(@Param("roomId") int roomId);

    // 5. 메시지 저장
    void insertMessage(ChatMessageDTO dto);

    // 6. 권한 + 부서로 관리자 1명 조회
    String findStaffIdByChatType(@Param("chatType") String chatType);
}
