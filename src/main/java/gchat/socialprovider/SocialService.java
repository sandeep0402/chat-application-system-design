package gchat.socialprovider;

import gchat.user.User;

public interface SocialService {
    String getRedirectUrlForLogin();
    User doLoginCallback(String authorizationCode);
}
