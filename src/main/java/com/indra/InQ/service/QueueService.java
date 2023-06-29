package com.indra.InQ.service;

import com.indra.InQ.common.Common;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueService {
    @Autowired
    QueueRepo queueRepo;

    @Autowired
    EntityService entityService;

    @Autowired
    Common common;
    public QueueModal createNewQueue(QueueDescription queueDescription){
        QueueModal queueModal=new QueueModal();
        queueModal.setName(queueDescription.getName());
        queueModal.setId(queueDescription.getId());
        queueModal.setStartRange(queueDescription.getStartRange());
        queueModal.setEndRange(queueDescription.getEndRange());
        queueModal.setMaxInQueueLimit(queueDescription.getMaxInQueueLimit());
        queueModal.setDescription(queueDescription.getDescription());
        queueRepo.save(queueModal);
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
    public boolean addQueueCheck(List<QueueDescription>queueDescriptions,String entityId){
        if(entityService.findUserByEntityId(entityId)==null)
            throw new ApiRequestException("Invalid entityId");
//      Name check
        Map<String,Integer> nameMap=new HashMap<>();
        for(int i=0;i<queueDescriptions.size();i++) {
            String qName=queueDescriptions.get(i).getName();
            if(nameMap.get(qName)!=null && nameMap.get(qName).equals(1))
                throw new ApiRequestException("queue description name can not be same");
            nameMap.put(qName,1);
            String qId=common.createQueueId(entityId,qName);
            if(getQueueById(qId)!=null)
                throw new ApiRequestException("Invalid queue description, queue id already exists");
        }
        return true;
    }
    public List<String> addQueueByQueueDescription(List<QueueDescription>queueDescriptions,String entityId){
        List<String> responseQueueId=new ArrayList<>();
        if(addQueueCheck(queueDescriptions,entityId)==false)
            throw new ApiRequestException("Invalid queue description");
        for(int i=0;i<queueDescriptions.size();i++) {
            String queueId=common.createQueueId(entityId, queueDescriptions.get(i).getName());
            queueDescriptions.get(i).setId(queueId);
            createNewQueue(queueDescriptions.get(i));
            entityService.updateQueueIds(queueId,entityId);
            responseQueueId.add(queueId);
        }
        return responseQueueId;
    }

    public void removeQueueByQueueIdList(List<String> queueIdList, String entityId) {
        Entity entity=entityService.findUserByEntityId(entityId);
        if(entity==null)
            throw new ApiRequestException("Invalid entityId");
        else{
            for(int i=0;i<queueIdList.size();i++){
                if(!entity.getQueueIds().contains(queueIdList.get(i)))
                    throw new ApiRequestException("queueId not in entityId");
            }
        }
        for(int i=0;i<queueIdList.size();i++) {
            removeQueueById(queueIdList.get(i));
            entityService.updateQueueIds(queueIdList.get(i),entityId,"remove");
        }
    }
}
