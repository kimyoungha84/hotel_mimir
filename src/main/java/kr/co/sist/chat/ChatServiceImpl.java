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
    // 채팅방 생성 또는 기존 방 조회 (user_num, chat_type, staff 매칭)
    public ChatRoomDTO getOrCreateChatRoom(int user_num, String chat_type) {
        String staff_id = null;
        // chat_type에 따라 권한 조건 다르게 적용
        if ("0".equals(chat_type)) {
            // 객실 문의: room, inquiry 모두 있는 ACTIVE 관리자 중 랜덤 1명
            staff_id = chatRoomMapper.findRandomStaffWithPermissions("room", "inquiry");
        } else if ("1".equals(chat_type)) {
            // 다이닝 문의: dining, inquiry 모두 있는 ACTIVE 관리자 중 랜덤 1명
            staff_id = chatRoomMapper.findRandomStaffWithPermissions("dinning", "inquiry");
        } else if ("2".equals(chat_type)) {
            // 일반 문의: inquiry만 있는 ACTIVE 관리자 중 랜덤 1명
            staff_id = chatRoomMapper.findRandomStaffWithOnlyInquiry();
        }
        // staff_id, user_num, chat_type 로그 출력
        System.out.println("[채팅방 생성] user_num=" + user_num + ", chat_type=" + chat_type + ", staff_id=" + staff_id);

        // 필수값 null 체크
        if (staff_id == null || user_num == 0 || chat_type == null) {
            System.out.println("[채팅방 생성 실패] 필수값 누락: user_num=" + user_num + ", staff_id=" + staff_id + ", chat_type=" + chat_type);
            return null;
        }

        ChatRoomDTO room = chatRoomMapper.findByUserAndStaffAndType(user_num, staff_id, chat_type);
        if (room == null) {
            room = new ChatRoomDTO();
            room.setUser_num(user_num);
            room.setStaff_id(staff_id);
            // chat_type에 따라 dept_iden 설정
            switch (chat_type) {
                case "0":
                    room.setDept_iden("room");
                    break;
                case "1":
                    room.setDept_iden("dinning");
                    break;
                case "2":
                    room.setDept_iden("inquiry");
                    break;
                default:
                    room.setDept_iden("inquiry");
            }
            room.setChat_type(chat_type);

            // insert 직전 null 체크
            if (room.getUser_num() == 0 || room.getStaff_id() == null || room.getDept_iden() == null || room.getChat_type() == null) {
                System.out.println("[채팅방 INSERT 실패] 파라미터 null: " + room);
                return null;
            }
            System.out.println("[채팅방 INSERT 직전] " + room);
            chatRoomMapper.insert(room);

            // insert 후 DB 저장 확인
            ChatRoomDTO inserted = chatRoomMapper.findByUserAndStaffAndType(user_num, staff_id, chat_type);
            System.out.println("[채팅방 DB 저장 확인] " + inserted);
            if (inserted == null) {
                System.out.println("[채팅방 생성 실패] DB insert 후 조회 결과 null");
                return null;
            }
            return inserted;
        }
        return room;
    }

    @Override
    // 채팅방의 모든 메시지 조회
    public List<ChatMessageDTO> getMessagesByRoomId(int room_id) {
        return chatMessageMapper.findByRoomId(room_id);
    }

    @Override
    // 메시지 저장
    public void saveMessage(ChatMessageDTO message) {
        chatMessageMapper.insert(message);
    }

    @Override
    // staff_id로 담당 채팅방 리스트 조회
    public List<ChatRoomDTO> getRoomsByStaffId(String staff_id) {
        return chatRoomMapper.findRoomsByStaffId(staff_id);
    }

    @Override
    // user_num으로 사용자의 채팅방 리스트 조회
    public List<ChatRoomDTO> getRoomsByUserId(int user_num) {
        return chatRoomMapper.findRoomsByUserId(user_num);
    }
}
