package kr.co.sist.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Override
    public int getOrCreateRoom(Long userNum, String chatType) {
        Integer roomId = chatMapper.findRoomIdByUserAndType(userNum, chatType);
        if (roomId != null) {
            return roomId;
        }

        // 관리자 조회 (STAFF_ID만 반환)
        String staffId = chatMapper.findStaffIdByChatType(chatType);
        if (staffId == null) {
            throw new RuntimeException("배정 가능한 관리자가 없습니다.");
        }

        // 부서 정보도 채팅방에 들어가야 하므로, STAFF 테이블에서 한번 더 조회
        // 간단하게는 getRoomInfo 이후 채팅 시 사용
        ChatRoomDTO room = new ChatRoomDTO();
        room.setUser_num(userNum.intValue()); // ChatRoomDTO에 int로 선언되었다면
        room.setStaff_id(staffId);
        room.setDept_iden(
            chatType.equals("0") ? "room" :
            chatType.equals("1") ? "dinning" : "inquiry"
        );
        room.setChat_type(chatType);

        chatMapper.insertChatRoom(room);

        return room.getRoom_id(); // useGeneratedKeys 설정으로 자동 채번됨
    }

    @Override
    public List<ChatMessageDTO> loadMessages(Long userNum, String chatType) {
        Integer roomId = chatMapper.findRoomIdByUserAndType(userNum, chatType);
        if (roomId == null) {
            return List.of(); // 메시지 없음
        }
        return chatMapper.findMessagesByRoomId(roomId);
    }

    @Override
    public void saveMessage(ChatMessageDTO dto) {
        chatMapper.insertMessage(dto);
    }
}
