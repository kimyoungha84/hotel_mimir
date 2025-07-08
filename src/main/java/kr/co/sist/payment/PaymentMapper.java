package kr.co.sist.payment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    List<PaymentDTO> selectAllPayment();

    PaymentDTO selectOnePayment(int paymentNum);
    
    void insertPayment(PaymentDTO pDTO);
    
    void updatePayment(PaymentDTO pDTO);
   
}
