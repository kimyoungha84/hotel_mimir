package kr.co.sist.admin.room;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesSummaryDTO {
    private String typeName;
    private int memberCount;
    private int nonMemberCount;
    private int checkoutCount;
    private int checkinCount;
    private int completedCount;
    private int stayCount;
    private int cancelCount;
    private int checkoutAmount;
    private int checkinAmount;
    private int completedAmount;
    private int cancelAmount;
    private int totalAmount;
}//class
