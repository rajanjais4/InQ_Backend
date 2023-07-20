package com.indra.InQ.common;

import com.indra.InQ.modal.entity.EntityType;
import com.indra.InQ.modal.entity.Entity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Common {
    public String createEntityId(Entity entity) {
        EntityType entityType =entity.getEntityType();
        if(entityType == EntityType.restaurant)
            return "E_"+"RES_"+entity.getPhoneNumber();
        else
            return "E_"+"OTH_"+entity.getPhoneNumber();
    }

    public String createQueueId(String entityId, @NonNull String name) {
        return "Q"+"_"+entityId+"_"+name;
    }
    public String createUserId(Long phoneNumber) {
        return "U"+"_"+phoneNumber;
    }


}
