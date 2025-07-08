package kr.co.sist.nonmember;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NonMemberMapper {
    List<NonMemberDTO> selectAllNonMember();
    
    NonMemberDTO selectOneNonMember(int nonMemNum);
    
    void insertNonMember(NonMemberDTO nonDTO);
    
    void updateNonMember(NonMemberDTO nonDTO);
   
}
