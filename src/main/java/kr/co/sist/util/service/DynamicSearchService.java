package kr.co.sist.util.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.SortConditionBuilder.SortParam;
import kr.co.sist.util.domain.SearchDataDomain;
import kr.co.sist.util.mapper.DynamicSearchMapper;

@Service
public class DynamicSearchService {
    
    private static final Logger logger = LoggerFactory.getLogger(DynamicSearchService.class);
    
    @Autowired 
    private DynamicSearchMapper mapper;

    /**
     * FilterConfig에 따른 통합 검색 메서드
     */
    public List<SearchDataDomain> searchByFilterConfig(FilterConfig config, 
                                                      List<FilterCondition> filters, 
                                                      int offset, 
                                                      int end, 
                                                      int pageSize) {
        try {
            return switch (config) {
                case DINING -> searchDining(filters, offset, end, pageSize);
                case FAQ -> searchFaq(filters, offset, end, pageSize);
                case STAFF -> searchStaff(filters, offset, end, pageSize);
                case DINING_RESV -> searchDiningResv(filters, offset, end, pageSize);
                case DINING_USER -> searchUserDining(filters, offset, end, pageSize);
                case ROOM_RESV -> searchRoomResv(filters, offset, end, pageSize);
                case MEMBER -> searchMember(filters, offset, end, pageSize); // 추가
                case DINING_SLOT -> searchDiningSlot(filters, offset, end, pageSize);
                default -> {
                    logger.warn("지원하지 않는 FilterConfig: {}", config);
                    yield List.of();
                }
            };
        } catch (Exception e) {
            logger.error("검색 중 오류 발생 - config: {}, offset: {}, end: {}", config, offset, end, e);
            return List.of();
        }
    }

    public List<SearchDataDomain> searchDining(List<FilterCondition> filters, int offset, int end, int pageSize) {
        try {
            logger.debug("다이닝 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
            return mapper.searchDining(filters, offset, end, pageSize);
        } catch (Exception e) {
            logger.error("다이닝 검색 중 오류 발생", e);
            return List.of();
        }
    }
    
    public List<SearchDataDomain> searchUserDining(List<FilterCondition> filters, int offset, int end, int pageSize) {
    	try {
    		logger.debug("다이닝 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		return mapper.searchUserDining(filters, offset, end, pageSize);
    	} catch (Exception e) {
    		logger.error("다이닝 검색 중 오류 발생", e);
    		return List.of();
    	}
    }
    
    public List<SearchDataDomain> searchDiningResv(List<FilterCondition> filters, int offset, int end, int pageSize) {
    	try {
    		logger.debug("다이닝 예약 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		return mapper.searchDiningResv(filters, offset, end, pageSize);
    	} catch (Exception e) {
    		logger.error("다이닝 예약 검색 중 오류 발생", e);
    		return List.of();
    	}
    }

    public List<SearchDataDomain> searchFaq(List<FilterCondition> filters, int offset, int end, int pageSize) {
        try {
            logger.debug("FAQ 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
            return mapper.searchFaq(filters, offset, end, pageSize);
        } catch (Exception e) {
            logger.error("FAQ 검색 중 오류 발생", e);
            return List.of();
        }
    }
    
    public List<SearchDataDomain> searchStaff(List<FilterCondition> filters, int offset, int end, int pageSize) {
        try {
            logger.debug("직원 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
            for(FilterCondition filter : filters) {
            System.out.println(filter);
            }
            return mapper.searchStaff(filters, offset, end, pageSize);
        } catch (Exception e) {
            logger.error("직원 검색 중 오류 발생", e);
            return List.of();
        }
    }
    public List<SearchDataDomain> searchRoomResv(List<FilterCondition> filters, int offset, int end, int pageSize) {
    	try {
    		logger.debug("객실 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		for(FilterCondition filter : filters) {
    			System.out.println(filter);
    		}
    		return mapper.searchRoomResv(filters, offset, end, pageSize);
    	} catch (Exception e) {
    		logger.error("객실 검색 중 오류 발생", e);
    		return List.of();
    	}
    }
    public List<SearchDataDomain> searchDiningSlot(List<FilterCondition> filters, int offset, int end, int pageSize) {
    	try {
    		logger.debug("슬롯 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		for(FilterCondition filter : filters) {
    			System.out.println(filter);
    		}
    		return mapper.searchDiningSlot(filters, offset, end, pageSize);
    	} catch (Exception e) {
    		logger.error("슬롯 검색 중 오류 발생", e);
    		return List.of();
    	}
    }

    public List<SearchDataDomain> searchMember(List<FilterCondition> filters, int offset, int end, int pageSize) {
        try {
            logger.debug("멤버 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
            return mapper.searchMember(filters, offset, end, pageSize);
        } catch (Exception e) {
            logger.error("멤버 검색 중 오류 발생", e);
            return List.of();
        }
    }

    public List<SearchDataDomain> searchRoom(List<FilterCondition> filters, SortParam sp, int offset, int end, int pageSize, String checkIn, String checkOut) {
    	try {
    		logger.debug("객실 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		return mapper.searchRoom(filters, sp, offset, end, pageSize, checkIn, checkOut);
    	} catch (Exception e) {
    		logger.error("객실 검색 중 오류 발생", e);
    		return List.of();
    	}
    }
    
    public List<SearchDataDomain> searchRoomSales(List<FilterCondition> filters, int offset, int end, int pageSize, String startDate, String endDate) {
    	try {
    		logger.debug("객실 매출 검색 - filters: {}, offset: {}, end: {}", filters.size(), offset, end);
    		return mapper.searchRoomSales(filters, offset, end, pageSize, startDate, endDate);
    	} catch (Exception e) {
    		logger.error("객실 매출 검색 중 오류 발생", e);
    		return List.of();
    	}
    }
    /**
     * FilterConfig에 따른 통합 카운트 메서드
     */
    public int countByFilterConfig(FilterConfig config, List<FilterCondition> filters) {
        try {
            return switch (config) {
                case DINING -> countDining(filters);
                case FAQ -> countFaq(filters);
                case STAFF -> countStaff(filters);
                case DINING_RESV -> countDiningResv(filters);
                case ROOM_RESV -> countRoomResv(filters);
                case MEMBER -> countMember(filters); // 추가
                case DINING_SLOT -> countDiningSlot(filters);
                default -> {
                    logger.warn("지원하지 않는 FilterConfig: {}", config);
                    yield 0;
                }
            };
        } catch (Exception e) {
            logger.error("카운트 조회 중 오류 발생 - config: {}", config, e);
            return 0;
        }
    }
    
    public int countFaq(List<FilterCondition> filters) {
        try {
            return mapper.countFaq(filters);
        } catch (Exception e) {
            logger.error("FAQ 카운트 조회 중 오류 발생", e);
            return 0;
        }
    }

    public int countDining(List<FilterCondition> filters) {
        try {
            return mapper.countDining(filters);
        } catch (Exception e) {
            logger.error("다이닝 카운트 조회 중 오류 발생", e);
            return 0;
        }
    }
    
    public int countStaff(List<FilterCondition> filters) {
        try {
            return mapper.countStaff(filters);
        } catch (Exception e) {
            logger.error("직원 카운트 조회 중 오류 발생", e);
            return 0;
        }
    }
    
    public int countDiningResv(List<FilterCondition> filters) {
    	try {
    		return mapper.countDiningResv(filters);
    	} catch (Exception e) {
    		logger.error("다이닝예약 카운트 조회 중 오류 발생", e);
    		return 0;
    	}
    }
    
    public int countDiningSlot(List<FilterCondition> filters) {
    	try {
    		return mapper.countDiningSlot(filters);
    	} catch (Exception e) {
    		logger.error("다이닝슬롯 카운트 조회 중 오류 발생", e);
    		return 0;
    	}
    }
    
    public int countRoomResv(List<FilterCondition> filters) {
    	try {
    		return mapper.countRoomResv(filters);
    	} catch (Exception e) {
    		logger.error("객실예약 카운트 조회 중 오류 발생", e);
    		return 0;
    	}
    }

    public int countMember(List<FilterCondition> filters) {
        try {
            return mapper.countMember(filters);
        } catch (Exception e) {
            logger.error("멤버 카운트 조회 중 오류 발생", e);
            return 0;
        }
        
      
    }
    
} // class
