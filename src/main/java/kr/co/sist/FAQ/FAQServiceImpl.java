package kr.co.sist.FAQ;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FAQServiceImpl implements FAQService {

    @Autowired
    private FAQMapper faqMapper;

    @Override
    public List<FAQDTO> selectAllFAQ() {
        return faqMapper.selectAllFAQ();
    }
    
    @Override
    public void insertFaq(FAQDTO faq) {
    	faqMapper.insertFaq(faq);
    }
    
    @Override
    public void deleteFaqs(List<Integer> faqNums) {
        faqMapper.deleteFaqs(faqNums);
    }
}
