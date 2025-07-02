package kr.co.sist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.dto.FAQDTO;

@Mapper
public interface FAQMapper {
    List<FAQDTO> selectAllFAQ();
    
    void insertFaq(FAQDTO faq);
    
    void deleteFaqs(List<Integer> faqNums);

}
