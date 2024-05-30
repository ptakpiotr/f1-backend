package zti.f1backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
         registry.addEndpoint("/addcomment");
         registry.addEndpoint("/addcomment").withSockJS();

         registry.addEndpoint("/deletecomment");
         registry.addEndpoint("/deletecomment").withSockJS();

         registry.addEndpoint("/score");
         registry.addEndpoint("/score").withSockJS();

         registry.addEndpoint("/like");
         registry.addEndpoint("/like").withSockJS();
    }
}
