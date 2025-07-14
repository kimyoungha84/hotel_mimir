package kr.co.sist.chat;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatRoomMapper {
    /**
     * 특정 부서와 권한을 모두 가진 관리자 한 명 조회
     */
    String selectOneStaffByDeptAndPermission(@Param("dept_iden") String dept_iden, @Param("permission_id_code") String permission_id_code);

    /**
     * 유저, 관리자, 문의유형으로 채팅방 조회
     */
    ChatRoomDTO findByUserAndStaffAndType(@Param("user_num") int user_num, @Param("staff_id") String staff_id, @Param("chat_type") String chat_type);

    /**
     * room_id로 채팅방 조회
     */
    ChatRoomDTO findByRoomId(@Param("room_id") int room_id);

    /**
     * 채팅방 생성
     */
    void insert(ChatRoomDTO room);

    /**
     * staff_id로 담당 채팅방 리스트 조회
     */
    java.util.List<ChatRoomDTO> findRoomsByStaffId(@Param("staff_id") String staff_id);
} 