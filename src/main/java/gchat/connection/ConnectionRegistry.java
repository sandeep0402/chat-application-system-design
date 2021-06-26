package gchat.connection;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class ConnectionRegistry {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final ConcurrentMap<String, String> connectionsMap = new ConcurrentHashMap();
	private final Random random = new Random();

	// get connection id from username
	public String getConnectionId(String user) {
		return connectionsMap.get(user);
	}

	// get details of all online users, whoever is connected has entry in map
	public ConcurrentMap<String, String> getAllConnections() {
		return connectionsMap;
	}

	// Add new user entry with username and session id
	public void registerNewConnection(String user, String connectionId) {
		Assert.notNull(user, "User must not be null");
		Assert.notNull(connectionId, "connection ID must not be null");
		connectionsMap.put(user, connectionId);
		publishProjectCreatedEvent(user);
	}

	// Once client connection is disconnected, we receive connection id which is disconnected
	// find the entry in map from value set and remove it
	public void unregisterConnectionId(String connectionId) {
		Assert.notNull(connectionId, "connection ID must not be null");
		connectionsMap.entrySet().removeIf(entry -> StringUtils.equals(connectionId, entry.getValue()));
	}

	void publishProjectCreatedEvent(final String username) {
		UserConnectedEvent entityCreatedEvent = new UserConnectedEvent(this, username);
		applicationEventPublisher.publishEvent(entityCreatedEvent);
	}

}