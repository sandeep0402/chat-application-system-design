package gchat.offline;

import gchat.chat.ChatService;
import gchat.chat.Message;
import gchat.user.User;
import gchat.user.UserService;
import gchat.usermessage.UserMessage;
import gchat.usermessage.UserMessagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfflineService {
    private final UserMessagesRepository userMessagesRepository;
    private final UserService userService;
    private final ChatService chatService;

    @Async
    public void processOfflineMessages(String username){
        delayProcessToAvoidRaceCondition();

        User user = userService.findByUsername(username);
        log.info("Offline messages check. User is present:" + (user==null));
        if(user != null){
            List<UserMessage> messages = userMessagesRepository.findAllByToAndDeliveryReceiptIsNull(user.getId());
            if(!messages.isEmpty()){
                messages.stream().forEach(m -> {
                    User fromUser = userService.findById(m.getFrom());
                    if(fromUser != null) {
                        Message message = Message.builder()
                                .fromUser(fromUser.getUsername())
                                .toUser(user.getUsername())
                                .content(m.getContent())
                                .localDateTime(m.getDateTime())
                                .id(m.getMessageId()).build();
                        chatService.deliverUserSpecificMessage(message, m.getMessageId());
                    }
                });

            }
        }
    }


    private void delayProcessToAvoidRaceCondition(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
