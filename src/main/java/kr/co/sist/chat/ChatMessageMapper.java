package kr.co.sist.chat;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    List<ChatMessageDTO> findByRoomId(@Param("room_id") int room_id);
    void insert(ChatMessageDTO message);
    void markAsRead(@Param("room_id") int room_id, @Param("staff_id") String staff_id);
    /**
     * 안읽은 메시지 개수 (특정 방, 특정 staff_id 기준)
     */
    int countUnreadByRoomAndStaff(@Param("room_id") int room_id, @Param("staff_id") String staff_id);
    /**
     * 안읽은 메시지들을 읽음 처리하고, 그 id 리스트를 반환
     */
    List<Integer> markAsReadAndReturnIds(@Param("room_id") int room_id, @Param("staff_id") String staff_id);
} 