package gchat.chat;

import gchat.groupmessage.GroupMessageService;
import gchat.user.User;
import gchat.user.UserService;
import gchat.configuration.MessageResource;
import gchat.configuration.exception.CustomException;
import gchat.auth.JwtTokenProvider;
import gchat.connection.ConnectionRegistry;
import gchat.lastseen.LastSeenService;
import gchat.usermessage.UserMessageService;
import gchat.uuid.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ConnectionRegistry connectionRegistry;
    private final SimpMessagingTemplate template;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMessageService userMessageService;
    private final GroupMessageService groupMessageService;
    private final MessageResource messageResource;
    private final UuidService uuidService;
    private final LastSeenService lastSeenService;

    public String sendMessageToGroup(Message message,String groupName, String authorizationCode) {
        validateJwtToken(authorizationCode);
        User fromUser = userService.getCurrentUser();

        String destination = "/group/"+groupName+"/listen";
        log.info("group message received: " + message +", destination: "+destination);
        String uuid = uuidService.getNextUuid();

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        template.convertAndSend(destination, message);

        groupMessageService.save(uuid, fromUser.getId(), groupName, message.getContent());
        return message.getContent();
    }

    public Set<String> getOnlineUsers(){
        return connectionRegistry.getAllConnections().keySet();
    }

    public void sendMessageToUser(Message message, String authorizationCode) {
        validateJwtToken(authorizationCode);

        String uuid = uuidService.getNextUuid();
        User toUser = userService.findByUsername(message.getToUser());
        if(toUser == null){
            throw new CustomException(messageResource.getMessage("error.user.notfound",
                    new String[]{message.getToUser()}), HttpStatus.BAD_REQUEST);
        }
        User fromUser = userService.getCurrentUser();

        message.setFromUser(fromUser.getUsername());
        message.setLocalDateTime(LocalDateTime.now());
        message.setId(uuid);
        userMessageService.save(uuid, fromUser.getId(), toUser.getId(), message.getContent());

        deliverUserSpecificMessage(message, uuid);
    }

    public void deliverUserSpecificMessage(Message message, String uuid) {
        String connectionId = connectionRegistry.getConnectionId(message.getToUser());
        if(connectionId != null) {
            StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.MESSAGE);
            headers.setMessageId(uuid);
            headers.setSessionId(connectionId);
            headers.setLeaveMutable(true);
            template.convertAndSendToUser(connectionId, "/topic/message", message, headers.getMessageHeaders());
        }
        log.info("User Message: "+ message);
    }

    private void validateJwtToken(String authorizationCode) {
        Authentication auth = jwtTokenProvider.getAuthentication(authorizationCode);
        SecurityContextHolder.getContext().setAuthentication(auth);
        lastSeenService.recordLastSeenDateTime(auth);
    }

}
