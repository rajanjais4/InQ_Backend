package com.indra.InQ.service.queueService;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.user.UserStatus;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class QueueUpdateService {
    public void addUserAtLast() {
    }

    public QueueModal postQueueUserUpdate(QueueModal queueModal) {
        queueModal.setQueueMovingRateInSeconds(500);
        return queueModal;
    }
    public UserStatus getUserStatusInQueue(@NonNull String userId, @NonNull QueueModal queueModal) {
        if((queueModal.getUserInQueueList()==null&&
                queueModal.getUserOutOfQueueList()==null) ||
                (queueModal.getUserOutOfQueueList().contains(userId)==false &&
                        queueModal.getUserInQueueList().contains(userId)==false))
            throw new ApiRequestException("userId not exists in queue");
        if(queueModal.getUserOutOfQueueList().contains(userId))
            return UserStatus.inEntity;
        else if(queueModal.getUserInQueueList().contains(userId)==false)
            return UserStatus.outQueue;
        else if(queueModal.getUserInQueueList().indexOf(userId)+1<=3)
            return UserStatus.readyToGo;
        return UserStatus.inQueue;
    }
}
