package com.fullstack.freelancer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstack.freelancer.model.dto.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
        private final SimpMessagingTemplate messagingTemplate;
        List<String> clientsSessionList = new ArrayList<>();
        List<String> adminSessionList = new ArrayList<>();


    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;

    }

    @MessageMapping("/chat")
    public void send(ChatMessage message, @Header("simpSessionId") String sessionId) throws Exception {

//        HERE I READ WHO HAS SENT THE MESSAGE BY SESSIONID , IF IT IS ADMIN I SHOULD FORWARD IT TO CLIENT SESSIONID,
//        BUT IF ITS CLIENT THEN I SHOULD FORWARD IT TO ADMIN SESSIONID
//        String sessionId = headerAccessor.getSessionId();
        System.out.println(sessionId);
        String otherUSer = "";
        for (String s : clientsSessionList) {
            if(!s.equals(sessionId)) otherUSer = s;
        }

            messagingTemplate.convertAndSend("/queue/messages/"+otherUSer,message);


    }



    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) throws JsonProcessingException {
//                if(adminSessionList.isEmpty()) adminSessionList.add("test");
        // event = SessionConnectedEvent[GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT_ACK,
        // simpConnectMessage=GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT,
        // nativeHeaders={accept-version=[1.1,1.0], heart-beat=[10000,10000]}, simpSessionAttributes={}, simpHeartbeat=[J@7d4dd2f4,
        // simpSessionId=y0vunx5w}], simpSessionId=y0vunx5w}]]

        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
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
        System.out.println("CLIENT " + sessionId + " CONNECTED");
        clientsSessionList.add(sessionId);
    }
//    @EventListener
//    public void handleSessionDisconnect(SessionDisconnectEvent event) {
//        String sessionId = event.getSessionId();
//       boolean isAdmin = adminSessionList.contains(sessionId);
//        System.out.printf("%s has DISCONNECTED sessionId is -> %s \n",isAdmin ? "ADMIN" : "CLIENT",sessionId);
//
//        if (isAdmin) adminSessionList.remove(sessionId);
//         else clientsSessionList.remove(sessionId);
//    }

}


