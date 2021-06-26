package gchat.offline;

import gchat.connection.UserConnectedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConnectedEventListener implements ApplicationListener<UserConnectedEvent> {
  private final OfflineService offlineService;

  @Override
  public void onApplicationEvent(UserConnectedEvent userConnectedEvent) {
    if (userConnectedEvent != null && StringUtils.isNotBlank(userConnectedEvent.getName())) {
      offlineService.processOfflineMessages(userConnectedEvent.getName());
    }
  }
}