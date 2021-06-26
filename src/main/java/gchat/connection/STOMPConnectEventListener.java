package gchat.connection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Slf4j
@Service
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    @Autowired
    private ConnectionRegistry sessionsStorage;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if(sha.getNativeHeader("login") != null) {
            //login get from browser
            String agentId = sha.getNativeHeader("login").get(0);
            String connectionId = sha.getSessionId();
            log.info("agentId: " + agentId + ", connectionId: " + connectionId);
            sessionsStorage.registerNewConnection(agentId, connectionId);
        }
    }
}