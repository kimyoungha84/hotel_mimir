package kr.co.sist.chat;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    List<ChatMessageDTO> findByRoomId(@Param("room_id") int room_id);
    void insert(ChatMessageDTO message);
    void markAsRead(@Param("room_id") int room_id, @Param("staff_id") String staff_id);
} 