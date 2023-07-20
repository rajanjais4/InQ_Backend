package com.indra.InQ.service.userService;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.user.request.AddUserToQueueRequest;
import com.indra.InQ.service.queueService.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueueMatchService {
    @Autowired
    QueueService queueService;
    public QueueModal getBestQueueMatchForUser(Entity entity, AddUserToQueueRequest addUserToQueueRequest) {
        if(entity==null || entity.getQueueIds()==null || entity.getQueueIds().size()==0)
            throw new ApiRequestException("No queue exists in the entity");
        List<String> validQueueIdsList=getValidQueueIdList(entity, addUserToQueueRequest);
        QueueModal queueModal=getShortestQueue(validQueueIdsList, addUserToQueueRequest.getUserId());
        if(queueModal==null)
            throw new ApiRequestException("No valid queue found to add");
        return queueModal;
    }

    private QueueModal getShortestQueue(List<String> validQueueIdsList,String userId) {
//        TODO: proper implementation using avg time;
        QueueModal queueModalResponse = null;
        int size=Integer.MAX_VALUE;
        List<QueueModal> validQueueList=queueService.getQueueByIdList(validQueueIdsList);
        for (int i=0;i<validQueueList.size();i++){
            QueueModal queueModal=validQueueList.get(i);

            if(queueModal.getUserInQueueList()==null
            ||queueModal.getUserInQueueList().size()==0
            ){
                return queueModal;
            } else if (queueModal.getUserInQueueList().size()<size &&
                    queueModal.getUserInQueueList().contains(userId)==false) {
                size=0;
                queueModalResponse=queueModal;
            }
        }
        return queueModalResponse;
    }

    private List<String> getValidQueueIdList(Entity entity, AddUserToQueueRequest addUserToQueueRequest) {
//        TODO: proper implementation of this function
        return entity.getQueueIds();
    }
}
