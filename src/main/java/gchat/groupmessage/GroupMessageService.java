package gchat.groupmessage;

import gchat.group.ChatGroup;
import gchat.group.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMessageService {
    private final GroupMessageRepository groupMessageRepository;
    private final ChatGroupRepository chatGroupRepository;

    @Async()
    public void save(String uuid, Long from, String groupName, String content){
        ChatGroup chatGroup = chatGroupRepository.findByName(groupName);
        if(chatGroup != null) {
            GroupMessage groupMessage = GroupMessage.builder()
                    .messageId(uuid)
                    .content(content)
                    .groupId(chatGroup.getId())
                    .from(from)
                    .dateTime(LocalDateTime.now())
                    .build();
            groupMessageRepository.save(groupMessage);
        }
    }

}
