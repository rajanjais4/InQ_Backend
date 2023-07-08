package com.indra.InQ.controller;

import com.indra.InQ.common.GenericWebsocketResponse;
import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.EntityQueueModal;
import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.modal.ws.EntityQueueUpdateRequestWs;
import com.indra.InQ.service.EntityService;
import com.indra.InQ.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class EntityController {
    @Autowired
    EntityService entityService;

    @Autowired
    QueueService queueService;
    @ApiIgnore
    @RequestMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {

        response.sendRedirect("/swagger-ui.html");
    }
    @PostMapping("/addNewEntity")
    public ResponseEntity<Entity> addNewEntity(@RequestBody Entity entity){
        entityService.saveNewUser(entity);
        return ResponseEntity.ok(entity);
    }
    @PostMapping("/updateEntity")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity){

        return ResponseEntity.ok(entityService.updateEntity(entity));
    }
    @GetMapping("/getEntityByPhoneNumber")
    public ResponseEntity<Entity> getEntityByPhoneNumber(@RequestParam("phoneNumber")Long phoneNumber){
        Entity entity= entityService.findUserByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(entity);
    }
    @GetMapping("/logInEntityByPhoneNumber")
    public ResponseEntity<EntityQueueModal> logInEntityQueueByPhoneNumber(@RequestParam("phoneNumber")Long phoneNumber,
                                                                     @RequestParam("password")String password){
        return ResponseEntity.ok(entityService.logInEntityQueueByPhoneNumber(phoneNumber,password));
    }

    @GetMapping("/getAllEntity")
    public ResponseEntity<List<Entity>> getAllEntity(){
        List<Entity> entityList= entityService.findAll();
        return ResponseEntity.ok(entityList);
    }
//    WS

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/updateEntityStatusWs")
    public void updateEntityStatusWs(@Payload EntityQueueUpdateRequestWs entityQueueUpdateRequestWs){
        try {
            System.out.println("updateEntityStatusWs Input - "+entityQueueUpdateRequestWs.toString());
            Entity entity= entityService.updateEntityStatus(entityQueueUpdateRequestWs.getEntityId(),
                    entityQueueUpdateRequestWs.getStatus());

            GenericWebsocketResponse genericWebsocketResponse=
                    new GenericWebsocketResponse(entityQueueUpdateRequestWs.getEntityId(),
                            Destination.entityUpdate,
                            entity,
                            ResponseStatus.success);

            simpMessagingTemplate.convertAndSendToUser(genericWebsocketResponse.getId(),
                    "/"+ genericWebsocketResponse.getDestination(),
                    genericWebsocketResponse);

        }
        catch (Exception e){
//            TODO: send error message by session ID
            System.out.println("error in updateQueueStatusWs - "+e.toString());
            throw new GenricWebsocketException(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityUpdate,e.getMessage());
        }
//        TODO: implement - update send to user
    }
}
