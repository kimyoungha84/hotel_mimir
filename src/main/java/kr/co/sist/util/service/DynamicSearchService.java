package kr.co.sist.util.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.domain.SearchDataDomain;
import kr.co.sist.util.mapper.DynamicSearchMapper;


@Service
public class DynamicSearchService implements DynamicSearchMapper{
    @Autowired private DynamicSearchMapper mapper;

	@Override
	public List<SearchDataDomain> searchDynamic(String table, List<String> columns, List<FilterCondition> filters) {
		return null;
	}


	@Override
	public List<SearchDataDomain> searchDining(List<FilterCondition> filters, int offset, int end, int pageSize) {
		return mapper.searchDining(filters, offset, end, pageSize);
	}


	@Override
	public List<SearchDataDomain> searchFaq(List<FilterCondition> filters, int offset, int end, int pageSize) {
		for(FilterCondition filter : filters) {
			System.out.println(filter);
			
		}
		return mapper.searchFaq(filters, offset, end, pageSize);
	}


	@Override
	public int countFaq(List<FilterCondition> filters) {
		return mapper.countFaq(filters);
	}


	@Override
	public int countDining(List<FilterCondition> filters) {
		return mapper.countDining(filters);
	}
	
	
	
}//class
