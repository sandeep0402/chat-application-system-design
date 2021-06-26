package gchat.connection;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class UserConnectedEvent extends ApplicationEvent {
  private String name;

  UserConnectedEvent(Object source, String name) {
    super(source);
    this.name = name;
  }
}
