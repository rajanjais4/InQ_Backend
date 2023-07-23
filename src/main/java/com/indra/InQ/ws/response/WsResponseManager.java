package com.indra.InQ.ws.response;

import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.ws.response.GenericWebsocketResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WsResponseManager {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public void sendGenericResponse(@NonNull String id,
                             @NonNull Destination destination,
                             @NonNull Object messageBody,
                             @NonNull ResponseStatus responseStatus){
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
    public void sendGenericResponse(@NonNull String id,
                             @NonNull Destination destination,
                             @NonNull Object messageBody,
                             @NonNull ResponseStatus responseStatus,
                             @NonNull String description){

    }
}
