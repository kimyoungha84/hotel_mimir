package kr.co.sist.chat;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ChatMessageDTO {
	
	private int message_id;
	private int room_id;
	private String staff_id;
	private String dept_iden;
	private String content;
	private Timestamp send_time;
	private String is_read;

}
