package com.indra.InQ.ws;

import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.common.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class WsResponseManager {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public void sendGenericResponse(String id,
                             Destination destination,
                             Object messageBody,
                             ResponseStatus responseStatus){
        if(responseStatus.equals(ResponseStatus.success)){
            GenericWebsocketResponse genericWebsocketResponse=
                    new GenericWebsocketResponse(id,
                            destination,
                            messageBody,
                            ResponseStatus.success);
            simpMessagingTemplate.convertAndSendToUser(genericWebsocketResponse.getId(),
                    "/"+ genericWebsocketResponse.getDestination(),
                    genericWebsocketResponse);
        }
        else
            throw new GenricWebsocketException(id,destination,messageBody.toString());
    }
    public void sendGenericResponse(String id,
                             Destination destination,
                             Object messageBody,
                             ResponseStatus responseStatus,
                             String description){

    }
}
