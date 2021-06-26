package gchat.connection;

import gchat.usermessage.UserMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserMessageService userMessageService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gchat-websockets").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/group/");
        config.setApplicationDestinationPrefixes("/gchat");
        config.setUserDestinationPrefix("/user");
        config.setPreservePublishOrder(true);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
          registration.interceptors(new ExecutorChannelInterceptor() {

          @Override
          public void afterMessageHandled(Message<?> inMessage,
                                          MessageChannel inChannel, MessageHandler handler, Exception ex) {

              StompHeaderAccessor inAccessor = StompHeaderAccessor.wrap(inMessage);
              String messageId = inAccessor.getFirstNativeHeader("message-id");
              if (StringUtils.isNotBlank(messageId)) {
                System.out.println("ACK received for messageId: " + messageId + " , Command: " + inAccessor.getCommand());
                userMessageService.ack(messageId);
              }
          }
      });
    }
}