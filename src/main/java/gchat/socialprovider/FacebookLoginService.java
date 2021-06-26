package gchat.socialprovider;

import gchat.user.User;
import org.springframework.stereotype.Service;

@Service
public class FacebookLoginService implements SocialService {
    @Override
    public String getRedirectUrlForLogin() {
        return null;
    }

    @Override
    public User doLoginCallback(String authorizationCode) {
        return null;
    }

}
