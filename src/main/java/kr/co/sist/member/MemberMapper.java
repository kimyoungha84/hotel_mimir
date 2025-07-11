package kr.co.sist.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
		
		int insertMember(MemberDTO mDTO);
		
		int countByEmail(String email_id);
		
		MemberDTO selectMemberByEmail(String email_id);
		
		void updateRefreshToken(MemberDTO mDTO);
}
