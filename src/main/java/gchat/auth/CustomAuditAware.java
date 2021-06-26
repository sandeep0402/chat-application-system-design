package gchat.auth;

import gchat.user.UserService;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CustomAuditAware implements AuditorAware<gchat.user.User> {
    private UserService userService;

    public CustomAuditAware(UserService userService){
        this.userService = userService;
    }

    @Override
    public Optional<gchat.user.User> getCurrentAuditor() {
        return Optional.of(userService.getCurrentUser());
    }
}