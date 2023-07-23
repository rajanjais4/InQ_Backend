package com.indra.InQ.service.queueService;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.user.UserAction;
import com.indra.InQ.modal.user.UserStatus;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class QueueUpdateService {
    UserAction userAction;

    public QueueModal postQueueUserUpdate(QueueModal queueModal) {
        queueModal.setQueueMovingRateInSeconds(500);
        return queueModal;
    }
    public UserStatus getUserStatusInQueue(@NonNull String userId, @NonNull QueueModal queueModal) {
        if((queueModal.getUserInQueueList()==null&&
                queueModal.getUserInEntityList()==null) ||
                (queueModal.getUserInEntityList().contains(userId)==false &&
                        queueModal.getUserInQueueList().contains(userId)==false))
            throw new ApiRequestException("userId not exists in queue");
        if(queueModal.getUserInEntityList().contains(userId))
            return UserStatus.inEntity;
        else if(queueModal.getUserInQueueList().contains(userId))
            return UserStatus.inQueue;
        else if(queueModal.getUserWithQrGenerated().contains(userId))
            return UserStatus.readyToGo;
        return UserStatus.outQueue;
    }

    public QueueModal addNewUserInQueue(QueueModal queueModal, String userId) {
        if(queueModal.getUserInQueueList().contains(userId)
                || queueModal.getUserWithQrGenerated().contains(userId)) {
            if(userAction.equals(UserAction.add))
                throw new ApiRequestException("User already in queue");
        }
        if(queueModal.getUserWithQrGenerated().size()<3&&queueModal.getUserInQueueList().size()==0)
            queueModal.getUserWithQrGenerated().add(userId);
        else
            queueModal.getUserInQueueList().add(userId);
        return queueModal;
    }
}
