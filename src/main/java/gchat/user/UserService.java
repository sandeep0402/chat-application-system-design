package gchat.user;

import gchat.auth.JwtTokenProvider;
import gchat.configuration.MessageResource;
import gchat.configuration.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final MessageResource messageResource;

  public String signin(String username, String password) {
    try {
      User user = userRepository.findByUsername(username);
      if (user == null) {
        user = new User(username, password);
        signup(user);
      }else {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      }
      return jwtTokenProvider.createToken(username, user.getRoles());
    } catch (AuthenticationException e) {
      e.printStackTrace();
      throw new CustomException(messageResource.getMessage("error.login"), HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void signup(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));
    userRepository.save(user);
  }

  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
  }

  public User findByUsername(String username){
    return userRepository.findByUsername(username);
  }

  public User findById(Long id){
    return userRepository.findById(id);
  }

  public User getCurrentUser(){
    return findByUsername(getCurrentUsername());
  }

  public String getCurrentUsername(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    String username = (((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
    return username;
  }

}
