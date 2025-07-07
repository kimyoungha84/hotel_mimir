package kr.co.sist.FAQ;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FAQMapper {
    List<FAQDTO> selectAllFAQ();
    
    void insertFaq(FAQDTO faq);
    
    void deleteFaqs(List<Integer> faqNums);
    
    FAQDTO selectOneFaq(int faq_num);
    
    void updateFaq(FAQDTO faq);
   
}
