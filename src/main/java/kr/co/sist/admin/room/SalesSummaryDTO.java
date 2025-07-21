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
    private int completedCount;
    private int cancelCount;
    private int totalAmount;
}//class
