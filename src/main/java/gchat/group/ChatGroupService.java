package gchat.group;

import gchat.user.User;
import gchat.user.UserService;
import gchat.chat.ChatGroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatGroupService {

    private final ChatGroupValidator chatGroupValidator;
    private final ChatGroupRepository chatGroupRepository;
    private final UserService userService;

    public Long create(String name){
        chatGroupValidator.validateValidName(name);
        chatGroupValidator.validateIfAbsent(name);

        ChatGroup group = ChatGroup.builder().name(name).build();
        group.addUser(userService.getCurrentUser());
        chatGroupRepository.save(group);
        return group.getId();
    }

    public void leave(String name){
        ChatGroup group = validateAndFetchGroup(name);
        chatGroupValidator.validateIfGroupMember(group);

        group.getUsers().remove(userService.getCurrentUser());
        chatGroupRepository.save(group);
    }

    public void join(String name){
        ChatGroup group = validateAndFetchGroup(name);
        chatGroupValidator.validateIfAlreadyGroupMember(group);

        group.addUser(userService.getCurrentUser());
        chatGroupRepository.save(group);
    }

    public List<NameIdPair> listGroups(boolean userSpecific){
        List<ChatGroup> chatGroups;
        if(userSpecific) {
            User currentUser = userService.getCurrentUser();
            chatGroups = chatGroupRepository.findAllByUsers_Id(currentUser.getId());
        }else{
            chatGroups = chatGroupRepository.findAll();
        }
        if(chatGroups != null){
            return chatGroups.stream().map(cg -> NameIdPair.builder().Id(cg.getId()).name(cg.getName()).build())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private ChatGroup validateAndFetchGroup(String name){
        chatGroupValidator.validateEmptyName(name);
        chatGroupValidator.validateIfExists(name);
        return chatGroupRepository.findByName(name);
    }
}
