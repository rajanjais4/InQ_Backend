package com.indra.InQ.service;

import com.indra.InQ.common.Common;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.repository.EntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityService {
    @Autowired
    EntityRepo entityRepo;

    @Autowired
    QueueService queueService;
    @Autowired
    Common common;
    public List<Entity> findAll(){
        return entityRepo.findAll();
    }
    public Entity findUserByPhoneNumber(String phoneNumber)
    {
        Entity entityResponse = entityRepo.findByPhoneNumber(phoneNumber).orElse(null);
        if(entityResponse==null)
            System.out.println("EntityService.findUserByPhoneNumber - no entity found - "+phoneNumber);
        else
            System.out.println("EntityService.findUserByPhoneNumber entity found - "+phoneNumber);
        return entityResponse;
    }

    public Entity saveNewUser(Entity entity){
        System.out.println("entity to save - "+entity.getName());
        if(findUserByPhoneNumber(entity.getPhoneNumber())!=null)
        {
            String msg="Phone number already exists - "+entity.getPhoneNumber();
            System.out.println(msg);
            throw new ApiRequestException(msg);
        }

//        if(entityRepo.findByEmail(entity.getEmail()).orElse(null)!=null)
//        {
//            String msg="Email already exists - "+entity.getEmail();
//            System.out.println(msg);
//            throw new ApiRequestException(msg);
//        }
        System.out.println("Saving New User");
        String id=common.createPersonId(entity);
        entity.set_id(id);
//        Adding unique Id to queue description
        for(int i=0;entity.getQueueDescriptions()!=null && i<entity.getQueueDescriptions().size();i++) {
            entity.getQueueDescriptions().get(i).setId(common.createQueueId(entity, entity.getQueueDescriptions().get(i).getName()));
            queueService.createNewQueue(entity.getQueueDescriptions().get(i));
        }
        return entityRepo.save(entity);
    }
}
