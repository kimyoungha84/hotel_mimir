package kr.co.sist.FAQ;

import java.util.List;

public interface FAQService {
    List<FAQDTO> selectAllFAQ();
    
    void insertFaq(FAQDTO faq);
    
    void deleteFaqs(List<Integer> faqNums);
}

