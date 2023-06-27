package com.indra.InQ.common;

import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.common.Type;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Common {
    public String createPersonId(Entity entity) {
        Type type=entity.getType();
        if(type==Type.restaurant)
            return "E_"+"RES_"+entity.getPhoneNumber();
        else
            return "E_"+"OTH_"+entity.getPhoneNumber();
    }

    public String createQueueId(Entity entity, @NonNull String name) {
        return "Q"+"_"+entity.getPhoneNumber()+"_"+name;
    }
}
