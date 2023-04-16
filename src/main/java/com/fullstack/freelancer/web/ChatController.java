package com.fullstack.freelancer.web;

import com.fullstack.freelancer.model.dto.AdminDTO;
import com.fullstack.freelancer.model.dto.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;
@Controller
public class ChatController {
        private final SimpMessagingTemplate messagingTemplate;
        List<String> clientsSessionList = new ArrayList<>();
        List<String> adminSessionList = new ArrayList<>();

        private static final String DESTINATION_SEND = "/queue/messages/";
        private static final String DESTINATION_ARRIVE = "/chat";


    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;

    }

    @MessageMapping(DESTINATION_ARRIVE)
    public void send(ChatMessage message, @Header("simpSessionId") String sessionId) {


                sendToAdmin(sessionId);

            messagingTemplate.convertAndSend(DESTINATION_SEND + sessionId,message);


    }

    @EventListener
    public void handleSessionConnected(SessionSubscribeEvent event) {
                if(adminSessionList.isEmpty()) adminSessionList.add("test");

        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");


       AdminDTO adminDTO = new AdminDTO(!adminSessionList.isEmpty());

        System.out.println("CLIENT " + sessionId + " CONNECTED");
        clientsSessionList.add(sessionId);

        messagingTemplate.convertAndSend(DESTINATION_SEND + sessionId,adminDTO);


    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
       boolean isAdmin = adminSessionList.contains(sessionId);
        System.out.printf("%s has DISCONNECTED sessionId is -> %s \n",isAdmin ? "ADMIN" : "CLIENT",sessionId);

        adminSessionList.removeIf(s -> s.equals(sessionId));
        clientsSessionList.removeIf(s -> s.equals(sessionId));




//        messagingTemplate.convertAndSend(DESTIONATION_SEND + clientsSessionList.get(0) ,"ds");
    }


    private void sendToAdmin(String sessionId) {
        messagingTemplate.convertAndSend(DESTINATION_SEND + sessionId, "toAdmin");
    }

}


