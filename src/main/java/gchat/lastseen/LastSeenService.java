package gchat.lastseen;

import gchat.user.User;
import gchat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LastSeenService {
    private final LastSeenRepository lastSeenRepository;
    private final UserService userService;

    @Async()
    public void recordLastSeenDateTime(Authentication auth){
        String username = (((org.springframework.security.core.userdetails.User)
                auth.getPrincipal()).getUsername());
        User currentUser = userService.findByUsername(username);
        if(currentUser == null || currentUser.getId()==null){
            return;
        }
        String userId = currentUser.getId().toString();
        Optional<LastSeen> lastSeenObject = lastSeenRepository.findById(userId);
        LastSeen lastSeen;
        if(lastSeenObject.isPresent()){
            lastSeen = lastSeenObject.get();
            lastSeen.setLastSeenAt(LocalDateTime.now());
        }else{
            lastSeen = LastSeen.builder()
                    .userId(userId).lastSeenAt(LocalDateTime.now()).build();
        }
        log.info("user id: "+ userId +", Last seen record updated.");
        lastSeenRepository.save(lastSeen);
    }


}
