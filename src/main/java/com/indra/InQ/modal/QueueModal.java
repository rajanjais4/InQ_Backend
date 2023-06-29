package com.indra.InQ.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indra.InQ.modal.common.QueueDescription;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "queues")
public class QueueModal extends QueueDescription {
    List<String>userInQueueList=new ArrayList<>();
    List<String>userOutOfQueueList=new ArrayList<>();
    @JsonIgnore
    int maxUserOutOfQueueListSize=100;
}
