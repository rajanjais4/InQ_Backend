package com.indra.InQ.exception;

import com.indra.InQ.modal.common.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class GenricWebsocketException extends RuntimeException{
    @NonNull
    private String id;
    @NonNull
    private Destination destination;
    @NonNull
    private String message;
    private String code="500";
    public GenricWebsocketException(String id,Destination destination,String message ){
        this.id=id;
        this.destination=destination;
        this.message=message;
    }
}
