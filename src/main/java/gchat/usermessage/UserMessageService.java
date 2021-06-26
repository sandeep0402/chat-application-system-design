package gchat.usermessage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserMessageService {
    private final UserMessagesRepository userMessagesRepository;

    @Async()
    public UserMessage save(String uuid, Long from, Long to, String content){
        UserMessage userMessage = UserMessage.builder()
                .messageId(uuid)
                .content(content)
                .to(to)
                .from(from)
                .dateTime(LocalDateTime.now())
                .build();
        userMessagesRepository.save(userMessage);
        return userMessage;
    }

    @Async()
    public void ack(String uuid){
        delayUpdate();
        Optional<UserMessage> userMessage = userMessagesRepository.findById(uuid);
        if(userMessage.isPresent()){
            UserMessage message = userMessage.get();
            if(message.getDeliveryReceipt() == null) {
                message.setDeliveryReceipt(LocalDateTime.now());
                userMessagesRepository.save(message);
            }
        }else{
            log.info("User message not found: "+ uuid);
        }
    }

    private void delayUpdate(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
