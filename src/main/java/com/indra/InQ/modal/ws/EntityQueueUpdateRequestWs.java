package com.indra.InQ.modal.ws;

import com.indra.InQ.modal.common.Direction;
import com.indra.InQ.modal.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityQueueUpdateRequestWs {
    @NonNull
    private String entityId;
    private String queueId;
    private Direction moveDirection;
    private Status status;
}
