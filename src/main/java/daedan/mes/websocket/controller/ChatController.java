package daedan.mes.websocket.controller;

import com.google.gson.Gson;
import daedan.mes.websocket.model.ChatRoom;
import daedan.mes.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ws/chat")
public class ChatController {

    private final ChatService chatService;


    //@Autowired
    //private SimpMessageSendingOperations messagingTemplate;

    /*
    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public String processMessageFromClient() throws Exception {
       String name = new Gson().fromJson(message, Map.class).get("name").toString();
        //messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", name);
        //return name;
        return null;
    }
    
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

     */

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }

}
