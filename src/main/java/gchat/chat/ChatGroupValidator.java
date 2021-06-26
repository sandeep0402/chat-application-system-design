package gchat.chat;

import gchat.user.User;
import gchat.user.UserService;
import gchat.configuration.MessageResource;
import gchat.configuration.exception.CustomException;
import gchat.group.ChatGroup;
import gchat.group.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatGroupValidator {

    private final ChatGroupRepository chatGroupRepository;
    private final UserService userService;
    private final MessageResource messageResource;

    public void validateEmptyName(String value) {
        if (StringUtils.isBlank(value)) {
            throw new CustomException(messageResource.getMessage("error.group.name.empty"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateValidName(String value) {
        if (StringUtils.length(value)<4) {
            throw new CustomException(messageResource.getMessage("error.group.name.min"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateIfMember(String value) {
        User currentUser = userService.getCurrentUser();
        if(chatGroupRepository.findFirstByNameAndUsers_Id(value, currentUser.getId()) == null){
            throw new CustomException(messageResource.getMessage("error.group.member.unknown"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateIfExists(String value) {
        if (chatGroupRepository.findByName(value) == null) {
            throw new CustomException(messageResource.getMessage("error.group.unknown"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateIfAbsent(String value) {
        if (chatGroupRepository.findByName(value) != null) {
            throw new CustomException(messageResource.getMessage("error.group.exists"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateIfAlreadyGroupMember(ChatGroup group) {
        User currentUser = userService.getCurrentUser();
        if(group.getUsers().contains(currentUser)) {
            throw new CustomException(messageResource.getMessage("error.group.member.exists"), HttpStatus.BAD_REQUEST);
        }
    }

    public void validateIfGroupMember(ChatGroup group) {
        User currentUser = userService.getCurrentUser();
        if(!group.getUsers().contains(currentUser)) {
            throw new CustomException(messageResource.getMessage("error.group.member.notfound"), HttpStatus.BAD_REQUEST);
        }
    }

}