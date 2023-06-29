package com.indra.InQ.modal;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntityQueueModal {
    private Entity entity;
    private List<QueueModal> queueList;
}
