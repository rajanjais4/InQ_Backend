package com.indra.InQ.service.queueService;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.common.Direction;
import com.indra.InQ.modal.common.Status;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QueueCommonServices {
    public boolean queueSanityCheck(@NonNull Entity entity, @NonNull QueueModal queueModal) {
        String queueId=queueModal.getId();
        String entityId=entity.getId();
        if ( entity.getQueueIds().contains(queueId) == false)
            throw new ApiRequestException("queueId does not exist in entity");
        if ( queueModal.getEntityId().equals(entityId) == false)
            throw new ApiRequestException("entityId does not exist in queue");
        return true;
    }

    public boolean runningQueueUpdateSanityChecks(@NonNull Entity entity, @NonNull QueueModal queueModal) {
        queueSanityCheck(entity,queueModal);
        if(!entity.getStatus().equals(Status.running))
            throw new ApiRequestException("This Entity is not in running state");
        if(!queueModal.getStatus().equals(Status.running))
            throw new ApiRequestException("This entity/queue is not in running state");
        return true;
    }
    public boolean runningQueueMoveUpdateByClientChecks(@NonNull Entity entity,@NonNull QueueModal queueModal,@NonNull Direction direction) {
        runningQueueUpdateSanityChecks(entity,queueModal);
        if(direction.equals(Direction.forward)&&
                (queueModal.getUserInQueueList()==null
                        || queueModal.getUserInQueueList().size()==0))
            throw new ApiRequestException("Unable to move queue forward,no user in queue");
        if(direction.equals(Direction.backward)&&
                (queueModal.getUserInEntityList()==null
                        || queueModal.getUserInEntityList().size()==0))
            throw new ApiRequestException("Unable to move queue backward,no user in out-queue");
        return true;
    }

    public QueueModal initilizeQueueLists(QueueModal queueModal) {
        if(queueModal.getUserInQueueList()==null)
            queueModal.setUserInQueueList(new ArrayList<>());
        if(queueModal.getUserInEntityList()==null)
            queueModal.setUserInEntityList(new ArrayList<>());
        if(queueModal.getUserWithQrGenerated()==null)
            queueModal.setUserWithQrGenerated(new ArrayList<>());
        if(queueModal.getUserInEntityList()==null)
            queueModal.setUserInEntityList(new ArrayList<>());
        return queueModal;
    }
}
