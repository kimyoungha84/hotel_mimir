package kr.co.sist.room;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.admin.room.SalesSummaryDTO;

@Mapper
public interface RoomMapper {

	public List<RoomDTO> selectAllRoom();
	public RoomDTO selectOneRoom(int roomId);
	
	public int countAvailableRooms(RoomSearchDTO rsDTO);
	
	
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
	
	public List<SalesSummaryDTO> selectSalesSummary(String startDate, String endDate);
	
}//interface
