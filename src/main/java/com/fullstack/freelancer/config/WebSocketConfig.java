package com.fullstack.freelancer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/* we are configuring a simple message broker that will broadcast messages to all subscribed clients on the /topic destination.
 We also set the application destination prefix to /app. Additionally, we register a WebSocket endpoint at /chat using SockJS,
  which is a JavaScript library that provides a fallback for WebSocket connections in case they are not supported by the client. */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer {


//    @Bean
//    public SimpMessageHeaderAccessor headerAccessor() {
//        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
//        headerAccessor.setLeaveMutable(true);
//        return headerAccessor;
//    }

    @Bean
    public SimpMessagingTemplate messagingTemplate(MessageChannel clientOutboundChannel) {
        SimpMessagingTemplate template = new SimpMessagingTemplate(clientOutboundChannel);
        template.setUserDestinationPrefix("/user");
        return template;
    }
    @Override
    protected void configureStompEndpoints(StompEndpointRegistry registry) {
//         HERE I GET MESSAGES
        registry.addEndpoint("/chat").withSockJS();
    }

     @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
         config.enableSimpleBroker("/topic","/messages","/user");

         // Set prefix for endpoints the client will send messages to
         config.setApplicationDestinationPrefixes("/app");

         config.setUserDestinationPrefix("/user");
    }



//


}
