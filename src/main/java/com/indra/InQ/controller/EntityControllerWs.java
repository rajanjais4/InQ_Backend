package com.indra.InQ.controller;

import com.indra.InQ.ws.GenericWebsocketResponse;
import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.modal.entity.EntityQueueUpdateRequestWs;
import com.indra.InQ.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityControllerWs {

    @Autowired
    EntityService entityService;
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
