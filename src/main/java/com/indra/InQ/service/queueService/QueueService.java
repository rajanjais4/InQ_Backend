package com.indra.InQ.service.queueService;

import com.indra.InQ.common.Common;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.common.Direction;
import com.indra.InQ.modal.common.Status;
import com.indra.InQ.modal.queue.QueueDescription;
import com.indra.InQ.modal.user.UserAction;
import com.indra.InQ.modal.user.UserQueueInfo;
import com.indra.InQ.modal.user.UserStatus;
import com.indra.InQ.modal.user.response.UserQueueUpdateResponse;
import com.indra.InQ.repository.QueueRepo;
import com.indra.InQ.service.EntityService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class QueueService {
    @Autowired
    QueueRepo queueRepo;

    @Autowired
    EntityService entityService;

    @Autowired
    private Environment env;
    @Autowired
    Common common;

    @Autowired
    QueueUpdateService queueUpdateService;
    private void saveQueue(QueueModal queueModal){
        try{
            queueRepo.save(queueModal);
        }
        catch (Exception e){
            throw new ApiRequestException("Failed to save queue with error: "+e.getMessage());
        }
    }
    public QueueModal createNewQueue(QueueDescription queueDescription,String entityId){
        QueueModal queueModal=new QueueModal();
        queueModal.setName(queueDescription.getName());
        queueModal.setId(queueDescription.getId());
        queueModal.setStartRange(queueDescription.getStartRange());
        queueModal.setEndRange(queueDescription.getEndRange());
        queueModal.setMaxInQueueLimit(queueDescription.getMaxInQueueLimit());
        queueModal.setDescription(queueDescription.getDescription());
        queueModal.setStatus(queueDescription.getStatus());
        queueModal.setCategory(queueDescription.getCategory());
        queueModal.setEntityId(entityId);

        //            TODO: remove below test code
        queueModal.getUserInQueueList().add("user1");
        queueModal.getUserInQueueList().add("user2");
        queueModal.getUserOutOfQueueList().add("user3");
        queueModal.getUserOutOfQueueList().add("user4");
//            TODO: remove above test code

        saveQueue(queueModal);

        return queueModal;
    }
    public QueueModal getQueueById(String queueId){
        return queueRepo.findById(queueId).orElse(null);
    }
    public List<QueueModal> getQueueByIdList(List<String> queueIdList){
        List<QueueModal>queueModalList=new ArrayList<>();
        for(int i=0;queueIdList!=null && i<queueIdList.size();i++){
            QueueModal queueModal=getQueueById(queueIdList.get(i));
            if(queueModal==null)
                throw new ApiRequestException("invalid queueId - "+queueIdList.get(i));
            queueModalList.add(queueModal);
        }
        return queueModalList;
    }

    public void removeQueueById(String queueId){
        queueRepo.deleteById(queueId);
    }
    private boolean addQueueCheck(List<QueueDescription>queueDescriptions,String entityId){
        Entity entity=entityService.findUserByEntityId(entityId);
        if(entity==null)
            throw new ApiRequestException("Invalid entityId");
//      Name check and category check
        Map<String,Integer> nameMap=new HashMap<>();
        for(int i=0;i<queueDescriptions.size();i++) {
            String qName=queueDescriptions.get(i).getName();
            if(nameMap.get(qName)!=null && nameMap.get(qName).equals(1))
                throw new ApiRequestException("queue description name can not be same");
            nameMap.put(qName,1);
            String qId=common.createQueueId(entityId,qName);
            if(entity.getQueueIds()!=null && entity.getQueueIds().contains(qId))
                throw new ApiRequestException("Invalid queue description, queue id already exists in Entity");
            if(getQueueById(qId)!=null)
                throw new ApiRequestException("Invalid queue description, queue id already exists");
            if(queueDescriptions.get(i).getCategory()!=null
                    && !entity.getCategories().contains(queueDescriptions.get(i).getCategory()))
                throw new ApiRequestException("Category -"+queueDescriptions.get(i).getCategory()+" not found in entity");
        }

//        TODO: Add range check
        return true;
    }
    public List<QueueModal> addQueueByQueueDescription(List<QueueDescription>queueDescriptions,String entityId){
        List<QueueModal> responseQueues=new ArrayList<>();
        if(!addQueueCheck(queueDescriptions,entityId))
            throw new ApiRequestException("Invalid queue description");
        for(int i=0;i<queueDescriptions.size();i++) {
            String queueId=common.createQueueId(entityId, queueDescriptions.get(i).getName());
            queueDescriptions.get(i).setId(queueId);
            if(queueDescriptions.get(i).getCategory()==null)
                queueDescriptions.get(i).setStatus(Status.stopped);
            QueueModal queueModal=createNewQueue(queueDescriptions.get(i),entityId);
            try{
                entityService.updateQueueIds(queueId,entityId);
            }
            catch (Exception exception){
                removeQueueById(queueDescriptions.get(i).getId());
                throw new ApiRequestException("Internal server error: failed to update entity");
            }
            responseQueues.add(queueModal);
        }
        return responseQueues;
    }

    public void removeQueueByQueueIdList(List<String> queueIdList, String entityId) {
        Entity entity=entityService.findUserByEntityId(entityId);
        if(entity==null)
            throw new ApiRequestException("Invalid entityId");
        else{
            for(int i=0;i<queueIdList.size();i++){
                if(!entity.getQueueIds().contains(queueIdList.get(i)))
                    throw new ApiRequestException("queueId not in entity");
            }
        }
        for(int i=0;i<queueIdList.size();i++) {
            QueueModal queueModal=getQueueById(queueIdList.get(i));
            removeQueueById(queueIdList.get(i));
            try {
                entityService.updateQueueIds(queueIdList.get(i),entityId,"remove");
            }
            catch (Exception e){
                saveQueue(queueModal);
                throw new ApiRequestException("Not able to remove queue Id from entity");
            }
        }
    }

//    Queue update by client
    private boolean queueUpdateSanityChecks(@NonNull Entity entity, @NonNull QueueModal queueModal) {
        if(!entity.getStatus().equals(Status.running))
            throw new ApiRequestException("This Entity is not in running state");
        String queueId=queueModal.getId();
        String entityId=entity.getId();
        if ( entity.getQueueIds().contains(queueId) == false)
            throw new ApiRequestException("queueId does not exist in entity");
        if ( queueModal.getEntityId().equals(entityId) == false)
            throw new ApiRequestException("entityId does not exist in queue");
        return true;
    }
    private boolean queueMoveUpdateByClientChecks(@NonNull Entity entity,@NonNull QueueModal queueModal,@NonNull Direction direction) {
        queueUpdateSanityChecks(entity,queueModal);

        if(!queueModal.getStatus().equals(Status.running))
            throw new ApiRequestException("This entity/queue is not in running state");
        if(direction.equals(Direction.forward)&&
                (queueModal.getUserInQueueList()==null
                        || queueModal.getUserInQueueList().size()==0))
            throw new ApiRequestException("Unable to move queue forward,no user in queue");
        if(direction.equals(Direction.backward)&&
                (queueModal.getUserOutOfQueueList()==null
                        || queueModal.getUserOutOfQueueList().size()==0))
            throw new ApiRequestException("Unable to move queue backward,no user in out-queue");
        return true;
    }

    public QueueModal moveQueueForwardByOneStep(@NonNull String queueId,@NonNull String entityId,@NonNull Direction direction) {
        if(direction.equals(Direction.backward)&& !env.getProperty("queue.undo.allowed").equals("true")){
            throw new ApiRequestException("Undo not allowed");
        }
        Entity entity=entityService.findUserByEntityId(entityId);
        QueueModal queueModalDb=getQueueById(queueId);
        queueMoveUpdateByClientChecks(entity,queueModalDb,direction);
        if(direction.equals(Direction.forward)){
            queueModalDb.getUserOutOfQueueList().add(
                    queueModalDb.getUserInQueueList().get(0)
            );
            queueModalDb.getUserInQueueList().remove(0);
        }
        else if(direction.equals(Direction.backward)){
            Integer userOutOfQueueListSize=queueModalDb.getUserOutOfQueueList().size();
            queueModalDb.getUserInQueueList().add(0,
                    queueModalDb.getUserOutOfQueueList().get(userOutOfQueueListSize-1));
            queueModalDb.getUserOutOfQueueList().remove(userOutOfQueueListSize-1);
        }
        saveQueue(queueModalDb);
        return queueModalDb;
    }

    public QueueModal updateQueueStatus(@NonNull String queueId, @NonNull String entityId, @NonNull Status status) {
        Entity entity=entityService.findUserByEntityId(entityId);
        QueueModal queueModalDb=getQueueById(queueId);
        queueUpdateSanityChecks(entity,queueModalDb);
        queueModalDb.setStatus(status);
        saveQueue(queueModalDb);
        return queueModalDb;
    }

    public UserQueueUpdateResponse updateQueueByUserAction(@NonNull UserQueueInfo userQueueInfo,@NonNull String userId, @NonNull UserAction userAction) {
        QueueModal queueModal=getQueueById(userQueueInfo.getQueueId());
        Entity entity=entityService.findUserByEntityId(queueModal.getEntityId());
        queueUpdateSanityChecks(entity,queueModal);
        if(userAction.equals(UserAction.add))
            queueUpdateService.addUserAtLast();
        if(queueModal.getUserInQueueList()==null)
            queueModal.setUserInQueueList(new ArrayList<>());
        if(queueModal.getUserInQueueList().contains(userId)) {
            if(userAction.equals(UserAction.add))
            throw new ApiRequestException("User already in queue");
        }
        else if(!userAction.equals(UserAction.add)) {
            throw new ApiRequestException("User not in queue");
        }
        if(userAction.equals(UserAction.add))
        queueModal.getUserInQueueList().add(userId);

        queueModal=queueUpdateService.postQueueUserUpdate(queueModal);
        saveQueue(queueModal);
        UserStatus userStatusUpdate=queueUpdateService.getUserStatusInQueue(userId,queueModal);
        if(!userStatusUpdate.equals(userQueueInfo.getUserStatus())){
            userQueueInfo.setUserStatus(userStatusUpdate);
            userQueueInfo.setUserStatusChangeEpoch(Instant.now().getEpochSecond());
        }
        return new UserQueueUpdateResponse(userId,
                userQueueInfo,
                queueModal.getQueueMovingRateInSeconds(),
                queueModal.getUserInQueueList().size());
    }
    //    Queue update by client
}
