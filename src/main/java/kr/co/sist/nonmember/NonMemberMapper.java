package kr.co.sist.nonmember;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.FAQ.FAQDTO;

@Mapper
public interface NonMemberMapper {
    List<FAQDTO> selectAllFAQ();
    
    void insertFaq(FAQDTO faq);
    
    void deleteFaqs(List<Integer> faqNums);
    
    FAQDTO selectOneFaq(int faq_num);
    
    void updateFaq(FAQDTO faq);
   
}
