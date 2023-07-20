package com.indra.InQ.controller;

import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.queue.QueueDescription;
import com.indra.InQ.service.queueService.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
