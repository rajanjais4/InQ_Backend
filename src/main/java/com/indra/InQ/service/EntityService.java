package com.indra.InQ.service;

import ch.qos.logback.core.subst.Token;
import com.indra.InQ.common.Common;
import com.indra.InQ.exception.ApiException;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.common.Type;
import com.indra.InQ.repository.EntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityService {
    @Autowired
    EntityRepo entityRepo;

    @Autowired
    QueueService queueService;
    @Autowired
    Common common;
    @Autowired
    private Environment env;
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
        System.out.println("Saving New User");
        if(entity.getType()==null){
            entity.setType(Type.valueOf(env.getProperty("entity.type.default")));
        }
        String id=common.createPersonId(entity);
        entity.set_id(id);
        if(entity.getQueueIds()!=null)
        entity.getQueueIds().clear();
        return entityRepo.save(entity);
    }

    public Entity updateEntity(Entity entity) {
        Entity entityDb= findUserByPhoneNumber(entity.getPhoneNumber());
        if(entityDb==null){
            throw new ApiRequestException("invalid phone number");
        }
        if(entity.getAddress()!=null)
            entityDb.setAddress(entity.getAddress());
        if(entity.getName()!=null)
            entityDb.setName(entity.getName());
        if(entity.getPassword()!=null)
            entityDb.setPassword(entity.getPassword());
        if(entity.getSummary()!=null)
            entityDb.setSummary(entity.getSummary());
        if(entity.getEmail()!=null)
            entityDb.setEmail(entity.getEmail());
        if(entity.getType()!=null)
            entityDb.setType(entity.getType());
        return entityRepo.save(entityDb);
    }
    public boolean updateQueueIds(String queueId,String entityId){
        return updateQueueIds(queueId,entityId,"add");
    }
    public boolean updateQueueIds(String queueId,String entityId,String operation){
        Entity entityDb=entityRepo.findById(entityId).orElse(null);
        if(entityDb==null)
            throw new ApiRequestException("Invalid EntityId");
        if(operation=="add"){
            if(entityDb.getQueueIds()==null)
                entityDb.setQueueIds(new ArrayList<>());
            entityDb.getQueueIds().add(queueId);
        }
        else if(operation=="remove")
            entityDb.getQueueIds().remove(queueId);
        else
            throw new ApiRequestException("internal server error - operation is not valid");
        entityRepo.save(entityDb);
        return true;
    }

    public Entity findUserByEntityId(String entityId) {
        return entityRepo.findById(entityId).orElse(null);
    }
}
