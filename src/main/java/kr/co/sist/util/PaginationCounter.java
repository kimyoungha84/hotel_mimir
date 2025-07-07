package kr.co.sist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.sist.util.service.DynamicSearchService;


@Component
public class PaginationCounter {
	
	
	@Autowired 
    private DynamicSearchService service;
	
	
}
