package com.indra.InQ.ws.response;

import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.modal.common.Destination;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

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
    @NonNull
    private Long responseSendEpoch;
    public GenericWebsocketResponse(String id, Destination destination, Object messageBody , ResponseStatus responseStatus,String description){
        this.id=id;
        this.destination=destination;
        this.messageBody=messageBody;
        this.description=description;
        this.responseStatus=responseStatus;
        responseSendEpoch= Instant.now().getEpochSecond();
    }
    public GenericWebsocketResponse(String id, Destination destination, Object messageBody ,ResponseStatus responseStatus){
        this.id=id;
        this.destination=destination;
        this.messageBody=messageBody;
        this.responseStatus=responseStatus;
        responseSendEpoch= Instant.now().getEpochSecond();
    }
}
