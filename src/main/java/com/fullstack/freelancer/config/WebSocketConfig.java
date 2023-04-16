package com.fullstack.freelancer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/* we are configuring a simple message broker that will broadcast messages to all subscribed clients on the /topic destination.
 We also set the application destination prefix to /app. Additionally, we register a WebSocket endpoint at /chat using SockJS,
  which is a JavaScript library that provides a fallback for WebSocket connections in case they are not supported by the client. */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {



    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//         HERE I GET MESSAGES
        registry.addEndpoint("/chat").withSockJS();
    }

     @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
         config.enableSimpleBroker("/queue","/messages");

         // Set prefix for endpoints the client will send messages to
         config.setApplicationDestinationPrefixes("/app");

//         config.setUserDestinationPrefix("/user");
    }


}
