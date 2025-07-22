package kr.co.sist.payment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.resvroom.ReservationDTO;

@Mapper
public interface PaymentMapper {
    public List<PaymentDTO> selectAllPayment();

    public PaymentDTO selectOnePayment(int paymentNum);
    
    public int selectPaymentSeq();
    
    public void insertPayment(ReservationDTO rDTO);
    
    public void updatePayment(PaymentDTO pDTO);
   
    public void insertPayment2(DiningResvDTO dto);
}
