package com.indra.InQ.modal.queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "queues")
public class QueueModal extends QueueDescription {
    List<String>userInQueueList;
    List<String>userWithQrGenerated;
    List<String>userOutOfQueueList;
    List<String> userInEntityList;
    String entityId;
    private Integer queueMovingRateInSeconds;
    @JsonIgnore
    Integer maxUserOutOfQueueListSize=100;
}
