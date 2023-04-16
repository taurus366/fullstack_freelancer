package com.fullstack.freelancer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstack.freelancer.model.dto.AdminDTO;
import com.fullstack.freelancer.model.dto.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

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
    public void send(ChatMessage message, @Header("simpSessionId") String sessionId) {
        System.out.println(sessionId);
        String otherUSer = "";

        for (String s : clientsSessionList) {
            if(!s.equals(sessionId)) otherUSer = s;
        }

//            messagingTemplate.convertAndSend("/queue/messages/"+sessionId,message);
            messagingTemplate.convertAndSend("/queue/messages/" + sessionId,message);


    }




    @EventListener
    public void handleSessionConnected(SessionSubscribeEvent event) throws InterruptedException {
                if(adminSessionList.isEmpty()) adminSessionList.add("test");

        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");

        boolean isAdminOnline = !adminSessionList.isEmpty();



       AdminDTO adminDTO = new AdminDTO(isAdminOnline);

        System.out.println("CLIENT " + sessionId + " CONNECTED1");
        clientsSessionList.add(sessionId);

        messagingTemplate.convertAndSend("/queue/messages/"+sessionId,adminDTO);


    }
//    @EventListener
//    public void handleSessionDisconnect(SessionDisconnectEvent event) {
//        String sessionId = event.getSessionId();
//       boolean isAdmin = adminSessionList.contains(sessionId);
//        System.out.printf("%s has DISCONNECTED sessionId is -> %s \n",isAdmin ? "ADMIN" : "CLIENT",sessionId);
//
//        if (isAdmin) adminSessionList.remove(sessionId);
//         else clientsSessionList.remove(sessionId);
//        messagingTemplate.convertAndSend("/queue/messages/" + sessionId ,"ds");
//    }

}


