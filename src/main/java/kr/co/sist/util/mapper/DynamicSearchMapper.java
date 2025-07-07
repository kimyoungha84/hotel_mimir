package kr.co.sist.util.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.domain.SearchDataDomain;

@Mapper
public interface DynamicSearchMapper {
    List<SearchDataDomain> searchDynamic(@Param("table") String table,
                                            @Param("columns") List<String> columns,
                                            @Param(	"filters") List<FilterCondition> filters);

    List<SearchDataDomain> searchDining(@Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    List<SearchDataDomain> searchFaq( @Param("filters") List<FilterCondition> filters, int offset, int end, int pageSize);
    
    
    int countFaq(@Param("filters") List<FilterCondition> filters);
    int countDining(@Param("filters") List<FilterCondition> filters);

}
