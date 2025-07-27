package kr.co.sist.util.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.SortConditionBuilder.SortParam;
import kr.co.sist.util.domain.SearchDataDomain;

@Mapper
public interface DynamicSearchMapper {

    List<SearchDataDomain> searchDining(@Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchFaq( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchStaff( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchDiningResv( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchUserDining( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchRoomResv( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchMember(@Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchRoom(@Param("filters") List<FilterCondition> filters, @Param("sortParam") SortParam sortParam, int offset, int end, int pageSize, String checkIn, String checkOut);
    
    List<SearchDataDomain> searchRoomSales(@Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize, String startDate, String endDate);
    
    List<SearchDataDomain> searchDiningSlot(@Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    int countFaq(@Param("filters") List<FilterCondition> filters);
    int countDining(@Param("filters") List<FilterCondition> filters);
    int countStaff(@Param("filters") List<FilterCondition> filters);
    int countDiningResv(@Param("filters") List<FilterCondition> filters);
    int countRoomResv(@Param("filters") List<FilterCondition> filters);
    int countMember(@Param("filters") List<FilterCondition> filters);
    int countDiningSlot(@Param("filters") List<FilterCondition> filters);

}
