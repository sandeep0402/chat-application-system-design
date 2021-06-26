package gchat.chat;

import gchat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

	public static final String GROUP_CHAT_VIEW = "groupChat";
	public static final String USER_CHAT_VIEW = "userChat";

	private final UserService userService;
	private final ChatService chatService;
	private final ChatGroupValidator chatGroupValidator;

	@GetMapping("group/{groupName}/connect")
	public String connectToGroup(@PathVariable String groupName,ModelMap map){
		chatGroupValidator.validateEmptyName(groupName);
		chatGroupValidator.validateIfMember(groupName);
		map.addAttribute("defaultUser", userService.getCurrentUsername());
		map.addAttribute("groupName", groupName);
		return GROUP_CHAT_VIEW;
	}

	@MessageMapping("/group/{groupName}/send")
	public @ResponseBody String sendMessageToGroup(@Payload Message message,
											   @DestinationVariable String groupName,
												   Map<String,String> map) {
		chatGroupValidator.validateEmptyName(groupName);
		chatGroupValidator.validateIfExists(groupName);
		return chatService.sendMessageToGroup(message, groupName, map.get("authorizationCode"));
	}

	@GetMapping("chat/user")
	public String getClient(ModelMap map){
		map.addAttribute("defaultUser", userService.getCurrentUsername());
		return USER_CHAT_VIEW;
	}

	@GetMapping("chat/users")
	public @ResponseBody
	ResponseEntity<SortedSet<String>> getOnlineUsers(){
		SortedSet<String> users = new TreeSet(chatService.getOnlineUsers());
		return ResponseEntity.ok(users);
	}

	@MessageMapping("/chat/message")
	public void message(Message message, Map<String,String> map) {
		chatService.sendMessageToUser(message, map.get("authorizationCode"));
	}

}
