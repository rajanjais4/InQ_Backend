package com.indra.InQ.controller;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.Direction;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.modal.common.Status;
import com.indra.InQ.service.QueueService;
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
        userFun("server message");
        List<QueueModal> queues= queueService.getQueueByIdList(queueIds);
        return ResponseEntity.ok(queues);
    }
    @PutMapping ("/moveQueueByOneStep")
    public ResponseEntity<QueueModal> moveQueueByOneStep(@RequestParam("queueId")String queueId,
                                                       @RequestParam ("entityId")String entityId,
                                                         @RequestParam ("direction") Direction direction){
        QueueModal queue= queueService.moveQueueForwardByOneStep(queueId,entityId,direction);
        return ResponseEntity.ok(queue);
    }

    @PutMapping ("/updateQueueStatus")
    public ResponseEntity<QueueModal> updateQueueStatus(@RequestParam("queueId")String queueId,
                                                         @RequestParam ("entityId")String entityId,
                                                         @RequestParam ("status") Status status){
        QueueModal queue= queueService.updateQueueStatus(queueId,entityId,status);
        return ResponseEntity.ok(queue);
    }

//      WS
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/moveQueueByOneStepWs")
    @SendTo("/entity/public")
    public QueueModal moveQueueByOneStepWs(@Payload String str){
        System.out.println("moveQueueByOneStepWs hit - "+str);
        return new QueueModal();
    }

    @MessageMapping("/userMessage")
    public String userFun(@Payload String str){
        String userId = str;
        String destination = "/user";
        String message = "Hello, John!";
        System.out.println("userMessage userId  - "+userId);
        simpMessagingTemplate.convertAndSendToUser("abc","/user",message);// /private/abc/user
        return message;
    }
}
