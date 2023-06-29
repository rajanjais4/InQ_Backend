package com.indra.InQ.controller;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QueueController {
    @Autowired
    QueueService queueService;
    @PostMapping("/addQueueByQueueDescriptionList")
    public ResponseEntity<List<String>> addQueueByQueueDescriptionList(@RequestBody List<QueueDescription> queueDescriptionList,
                                                     @RequestParam ("entityId")String entityId){
        List<String> responseQueueIds= queueService.addQueueByQueueDescription(queueDescriptionList,entityId);
        return ResponseEntity.ok(responseQueueIds);
    }
    @PostMapping("/removeQueueByIdList")
    public ResponseEntity<String> addNewEntity(@RequestBody List<String> queueIdList,
                                                     @RequestParam ("entityId")String entityId){
        queueService.removeQueueByQueueIdList(queueIdList,entityId);
        return ResponseEntity.ok("successfully removed all queues");
    }
    @GetMapping("/getQueueById")
    public ResponseEntity<QueueModal> getEntityByPhoneNumber(@RequestParam("queueId")String queueId){
        QueueModal queue= queueService.getQueueById(queueId);
        if(queue==null){
            throw new ApiRequestException("queue Id not found");
        }
        return ResponseEntity.ok(queue);
    }
}
