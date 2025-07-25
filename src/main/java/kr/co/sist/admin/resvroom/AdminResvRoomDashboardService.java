package kr.co.sist.admin.resvroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminResvRoomDashboardService {
	
    @Autowired
    private AdminResvRoomDashboardMapper arrdm;

    public List<MonthCountDTO> getMonthlyReservationCount() {
        return arrdm.selectMonthlyReservationCount();
    }

    public List<MonthSalesDTO> getMonthlySales() {
        return arrdm.selectMonthlySales();
    }

    public List<RoomOccupancyDTO> getRoomOccupancy() {
        return arrdm.selectRoomOccupancyRate();
    }

    public List<MemberRatioDTO> getMemberNonMemberRatio() {
        return arrdm.selectMemberNonMemberRatio();
    }

    public TodayReservationStatusDTO getTodayReservationStatus() {
        return arrdm.selectTodayReservationStatus();
    }

    public List<PopularRoomDTO> getPopularRooms() {
        return arrdm.selectPopularRooms();
    }
    
}