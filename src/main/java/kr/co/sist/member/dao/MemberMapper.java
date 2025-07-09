package kr.co.sist.member.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.member.dto.MemberDTO;

@Mapper
public interface MemberMapper {
		void insertMember(MemberDTO mDTO);
		
		int countByEmail(String email_id);
}
