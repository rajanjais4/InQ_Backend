package com.indra.InQ.common;

import com.indra.InQ.modal.common.Destination;
import lombok.Data;
import lombok.NonNull;

@Data
public class GenericWebsocketResponse {
    @NonNull
    private String id;
    @NonNull
    private Destination destination;
    @NonNull
    private Object messageBody;
    private String description="";
    @NonNull
    private ResponseStatus responseStatus;
    public GenericWebsocketResponse(String id, Destination destination, Object messageBody , ResponseStatus responseStatus,String description){
        this.id=id;
        this.destination=destination;
        this.messageBody=messageBody;
        this.description=description;
        this.responseStatus=responseStatus;
    }
    public GenericWebsocketResponse(String id, Destination destination, Object messageBody ,ResponseStatus responseStatus){
        this.id=id;
        this.destination=destination;
        this.messageBody=messageBody;
        this.responseStatus=responseStatus;
    }
}
