package kr.co.sist.room;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoomMapper {

	public List<RoomDTO> selectAllRoom();
	public RoomDTO selectOneRoom(int roomId);
	
	
    public void updateRoom(@Param("typeName") String typeName,
            @Param("bedName") String bedName,
            @Param("viewName") String viewName,
            @Param("description") String description);

    public void updateRoomImg(@Param("typeName") String typeName,
     @Param("bedName") String bedName,
     @Param("imagePath") String imagePath);
	
	public Integer findRoomIdByNames(@Param("typeName") String typeName,
            @Param("bedName") String bedName,
            @Param("viewName") String viewName);
	
}//interface
