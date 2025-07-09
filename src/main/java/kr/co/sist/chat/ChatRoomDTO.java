package kr.co.sist.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ChatRoomDTO {
	
	private int room_id;
	private int user_num;
	private String staff_id;
	private String dept_iden;
	private String chat_type;

}