package kr.co.sist.room;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {

	public List<RoomDTO> selectAllRoom();
}//interface
