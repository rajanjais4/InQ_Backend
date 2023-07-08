package com.indra.InQ.controller;

import com.indra.InQ.common.GenericWebsocketResponse;
import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.modal.common.Direction;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.modal.common.Status;
import com.indra.InQ.modal.ws.EntityQueueUpdateRequestWs;
import com.indra.InQ.service.QueueService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
public class QueueController {
    @Autowired
    QueueService queueService;
    @PostMapping("/addQueueByQueueDescriptionList")
    public ResponseEntity<List<QueueModal>> addQueueByQueueDescriptionList(@RequestBody List<QueueDescription> queueDescriptionList,
                                                                      @RequestParam ("entityId")String entityId){
        List<QueueModal> responseQueues= queueService.addQueueByQueueDescription(queueDescriptionList,entityId);
        return ResponseEntity.ok(responseQueues);
    }
    @PostMapping("/removeQueueByIdList")
    public ResponseEntity<String> removeQueueByIdList(@RequestBody List<String> queueIdList,
                                                     @RequestParam ("entityId")String entityId){
        queueService.removeQueueByQueueIdList(queueIdList,entityId);
        return ResponseEntity.ok("successfully removed all queues");
    }
    @GetMapping("/getQueueByIdList")
    public ResponseEntity<List<QueueModal>> getQueueByIdList(@RequestParam("queueId")List<String> queueIds){
        List<QueueModal> queues= queueService.getQueueByIdList(queueIds);
        return ResponseEntity.ok(queues);
    }
//    @PutMapping ("/moveQueueByOneStep")
//    public ResponseEntity<QueueModal> moveQueueByOneStep(@RequestParam("queueId")String queueId,
//                                                       @RequestParam ("entityId")String entityId,
//                                                         @RequestParam ("direction") Direction direction){
//        QueueModal queue= queueService.moveQueueForwardByOneStep(queueId,entityId,direction);
//        return ResponseEntity.ok(queue);
//    }

//    @PutMapping ("/updateQueueStatus")
//    public ResponseEntity<QueueModal> updateQueueStatus(@RequestParam("queueId")String queueId,
//                                                         @RequestParam ("entityId")String entityId,
//                                                         @RequestParam ("status") Status status){
//        QueueModal queue= queueService.updateQueueStatus(queueId,entityId,status);
//        return ResponseEntity.ok(queue);
//    }

//      WS
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/moveQueueByOneStepWs")
    public void moveQueueByOneStepWs(@Payload EntityQueueUpdateRequestWs entityQueueUpdateRequestWs){
        System.out.println("moveQueueByOneStepWs Input - "+entityQueueUpdateRequestWs.toString());
        try {

            QueueModal queue= queueService.moveQueueForwardByOneStep(entityQueueUpdateRequestWs.getQueueId(),
                    entityQueueUpdateRequestWs.getEntityId(),
                    entityQueueUpdateRequestWs.getMoveDirection());

            GenericWebsocketResponse genericWebsocketResponse=
                    new GenericWebsocketResponse(entityQueueUpdateRequestWs.getEntityId(),
                            Destination.entityQueueUpdate,
                            queue,
                            ResponseStatus.success);

            simpMessagingTemplate.convertAndSendToUser(genericWebsocketResponse.getId(),
                    "/"+ genericWebsocketResponse.getDestination(),
                    genericWebsocketResponse);
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

            GenericWebsocketResponse genericWebsocketResponse=
                    new GenericWebsocketResponse(entityQueueUpdateRequestWs.getEntityId(),
                            Destination.entityQueueUpdate,
                            queue,
                            ResponseStatus.success);

            simpMessagingTemplate.convertAndSendToUser(genericWebsocketResponse.getId(),
                    "/"+ genericWebsocketResponse.getDestination(),
                    genericWebsocketResponse);

        }
        catch (Exception e){
//            TODO: send error message by session ID
            System.out.println("error in updateQueueStatusWs - "+e.toString());
            throw new GenricWebsocketException(entityQueueUpdateRequestWs.getEntityId(),
                    Destination.entityQueueUpdate,e.getMessage());
        }
//        TODO: implement - update send to user
    }
}
