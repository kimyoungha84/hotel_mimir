package kr.co.sist.chat;

import java.util.List;

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
    List<ChatRoomDTO> findRoomsByStaffId(@Param("staff_id") String staff_id);
    
    /**
     * user_num으로 사용자의 채팅방 리스트 조회
     */
    List<ChatRoomDTO> findRoomsByUserId(@Param("user_num") int user_num);

    /**
     * 권한 2개(room, inquiry 또는 dinning, inquiry) 모두 가진 ACTIVE 관리자 중 랜덤 1명
     */
    String findRandomStaffWithPermissions(@Param("perm1") String perm1, @Param("perm2") String perm2);

    /**
     * 권한이 inquiry만 있는 ACTIVE 관리자 중 랜덤 1명
     */
    String findRandomStaffWithOnlyInquiry();
} 