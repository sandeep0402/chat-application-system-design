package gchat.chat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Message {

	private String id;
	private String content;
	private String toUser;
	private String fromUser;
	private LocalDateTime localDateTime;
}
