package com.fullstack.freelancer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.freelancer.model.dto.AdminDTO;
import com.fullstack.freelancer.model.dto.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
        private final SimpMessagingTemplate messagingTemplate;
//        private SimpMessageHeaderAccessor headerAccessor;
//    private final Map<String, String> sessionToUserMap = new HashMap<>();
        List<String> clientsSessionList = new ArrayList<>();
        List<String> adminSessionList = new ArrayList<>();
        final String test = "";

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;

    }

    @MessageMapping("/chat")
//        @SendTo("/topic/messages")
    public void send(ChatMessage message,SimpMessageHeaderAccessor headerAccessor) {
//        HERE I READ WHO HAS SENT THE MESSAGE BY SESSIONID , IF IT IS ADMIN I SHOULD FORWARD IT TO CLIENT SESSIONID,
//        BUT IF ITS CLIENT THEN I SHOULD FORWARD IT TO ADMIN SESSIONID
        String sessionId = headerAccessor.getSessionId();
        System.out.println(sessionId);

        if(clientsSessionList.contains(sessionId)) {
            System.out.println("its Client");
        }else {
            System.out.println("its Admin");
        }
        GenericMessage<byte[]> message3 = new GenericMessage<byte[]>(new byte[]{65,66,67});
        String messageContent = "Hello, world!";
        byte[] payload = messageContent.getBytes();
        Message<byte[]> message2 = MessageBuilder.withPayload(payload).build();
        System.out.println(headerAccessor.getMessageHeaders());
            messagingTemplate.send("1/topic/messages",message3);

//            messagingTemplate.convertAndSend("1/topic/messages","asdas",headerAccessor.getMessageHeaders());

//        assert sessionId != null;
//        messagingTemplate.convertAndSendToUser("1", "/topic/messages","asdas");
//        messagingTemplate.convertAndSend("/user/topic/messages", payload);
//    return "as;
    }

    private SimpMessageHeaderAccessor generateNewOne(String destination, String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setLeaveMutable(true);
        headerAccessor.setDestination(destination);

        headerAccessor.setSubscriptionId(sessionId);
        headerAccessor.setSessionId(sessionId);

        return headerAccessor;
    }

//    @EventListener
//    public void handleSessionConnected(SessionConnectedEvent event) throws JsonProcessingException {
//                if(adminSessionList.isEmpty()) adminSessionList.add("test");
//        // event = SessionConnectedEvent[GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT_ACK,
//        // simpConnectMessage=GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT,
//        // nativeHeaders={accept-version=[1.1,1.0], heart-beat=[10000,10000]}, simpSessionAttributes={}, simpHeartbeat=[J@7d4dd2f4,
//        // simpSessionId=y0vunx5w}], simpSessionId=y0vunx5w}]]
//
//        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
//
//        boolean isAdminOnline = !adminSessionList.isEmpty();
//
//        System.out.println("Connected " + sessionId);
//
//
//        AdminDTO adminDTO = new AdminDTO(isAdminOnline);
//
////        String messageContent = adminDTO.;
//        ObjectMapper objectMapper = new ObjectMapper();
//        String messageContent = objectMapper.writeValueAsString(adminDTO);
//
//        byte[] payload = messageContent.getBytes();
//
//        String destination = "/topic/messages";
//
//        SimpMessageHeaderAccessor headerAccessor =  generateNewOne(destination,sessionId);
//
//        assert sessionId != null;
//        messagingTemplate.convertAndSendToUser(sessionId, destination,payload,headerAccessor.getMessageHeaders());
//
//        clientsSessionList.add(sessionId);
//    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
       boolean isAdmin = adminSessionList.contains(sessionId);
        System.out.printf("%s has DISCONNECTED sessionId is -> %s \n",isAdmin ? "ADMIN" : "CLIENT",sessionId);

        if (isAdmin) adminSessionList.remove(sessionId);
         else clientsSessionList.remove(sessionId);
    }

}


