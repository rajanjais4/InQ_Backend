package com.indra.InQ.controller;

import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.modal.entity.EntityQueueUpdateRequestWs;
import com.indra.InQ.service.queueService.QueueService;
import com.indra.InQ.ws.response.WsResponseManager;
import com.indra.InQ.ws.response.WsUserResponseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueControlWs {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    QueueService queueService;
    @Autowired
    WsResponseManager wsResponseManager;

    @Autowired
    WsUserResponseManager wsUserResponseManager;
    @MessageMapping("/moveQueueByOneStepWs")
    public void moveQueueByOneStepWs(@Payload EntityQueueUpdateRequestWs entityQueueUpdateRequestWs){
        System.out.println("moveQueueByOneStepWs Input - "+entityQueueUpdateRequestWs.toString());
        try {

            QueueModal queue= queueService.moveQueueForwardByOneStep(entityQueueUpdateRequestWs.getQueueId(),
                    entityQueueUpdateRequestWs.getEntityId(),
                    entityQueueUpdateRequestWs.getMoveDirection());

            // Entity Response
            wsResponseManager.sendGenericResponse(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,
                    queue,
                    ResponseStatus.success);
//        TODO: implement - update send to user
        }
        catch (Exception e){
//            TODO: send error message by session ID
            System.out.println("error in moveQueueByOneStepWs - "+e);
            throw new GenricWebsocketException(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,e.getMessage());
        }
    }


    @MessageMapping("/updateQueueStatusWs")
    public void updateQueueStatusWs(@Payload EntityQueueUpdateRequestWs entityQueueUpdateRequestWs){
        try {
            System.out.println("updateQueueStatusWs Input - "+entityQueueUpdateRequestWs.toString());
            QueueModal queue= queueService.updateQueueStatus(entityQueueUpdateRequestWs.getQueueId(),
                    entityQueueUpdateRequestWs.getEntityId(),
                    entityQueueUpdateRequestWs.getStatus());

            wsResponseManager.sendGenericResponse(entityQueueUpdateRequestWs.getEntityId(),
                            Destination.entityQueueUpdate,
                            queue,
                            ResponseStatus.success);
            wsUserResponseManager.sendMandatoryResponseToAllUserInQueue(queue);
        }
        catch (Exception e){
//            TODO: send error message by session ID
            System.out.println("error in updateQueueStatusWs - "+e.toString());
            throw new GenricWebsocketException(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,e.getMessage());
        }
    }


    @MessageMapping("/currentQueueSnapshot")
    public void currentQueueSnapshot(@Payload EntityQueueUpdateRequestWs entityQueueUpdateRequestWs){
        try {
            System.out.println("currentQueueSnapshot Input - "+entityQueueUpdateRequestWs.toString());
            QueueModal queue= queueService.currentQueueSnapshot(entityQueueUpdateRequestWs.getQueueId(),
                    entityQueueUpdateRequestWs.getEntityId());

            wsResponseManager.sendGenericResponse(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,
                    queue,
                    ResponseStatus.success);
        }
        catch (Exception e){
//            TODO: send error message by session ID
            System.out.println("error in currentQueueSnapshot - "+e);
            throw new GenricWebsocketException(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,e.getMessage());
        }
    }
}
