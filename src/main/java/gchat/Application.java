package gchat;

import gchat.groupmessage.GroupMessageRepository;
import gchat.user.User;
import gchat.user.UserService;
import gchat.auth.CustomAuditAware;
import gchat.lastseen.LastSeenRepository;
import gchat.usermessage.UserMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {UserMessagesRepository.class, LastSeenRepository.class, GroupMessageRepository.class})
})
@EnableAsync
public class Application implements CommandLineRunner {

  @Autowired
  private UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... params) throws Exception {
    //userService.populateBasicData();
  }

  @Bean
  RouterFunction<ServerResponse> routerFunction() {
    return route(GET("/"), req ->
            ServerResponse.temporaryRedirect(URI.create("swagger-ui.html")).build());
  }

  @Bean
  public AuditorAware<User> auditorAware(){
    return new CustomAuditAware(userService);
  }


}
