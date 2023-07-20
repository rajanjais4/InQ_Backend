package com.indra.InQ.modal.entity;

import com.indra.InQ.modal.queue.QueueModal;
import lombok.Data;

import java.util.List;

@Data
public class EntityQueueModal {
    private Entity entity;
    private List<QueueModal> queueList;
}
