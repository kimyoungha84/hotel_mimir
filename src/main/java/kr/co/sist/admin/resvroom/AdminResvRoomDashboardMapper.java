package kr.co.sist.admin.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminResvRoomDashboardMapper {
	
    public List<MonthCountDTO> selectMonthlyReservationCount();
    public List<MonthSalesDTO> selectMonthlySales();
    public List<RoomOccupancyDTO> selectRoomOccupancyRate();
    public TodayReservationStatusDTO selectTodayReservationStatus();
    public List<PopularRoomDTO> selectPopularRooms();
    public List<MemberRatioDTO> selectMemberNonMemberRatio();

}
