package com.indra.InQ.exception;

import com.indra.InQ.common.GenericWebsocketResponse;
import com.indra.InQ.common.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler( value={ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex){
        ApiException apiException=createAPiException(ex.getMessage(),HttpStatus.BAD_REQUEST,ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler( value={Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex){
        ApiException apiException=createAPiException(ex.getMessage(),HttpStatus.BAD_REQUEST,ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

    private ApiException createAPiException(String msg, HttpStatus httpStatus, ZonedDateTime time) {
        ApiException apiException=new ApiException(
                msg,
                httpStatus,
                time
        );
        return apiException;
    }

//    WS

@Autowired
private SimpMessagingTemplate simpMessagingTemplate;
@ExceptionHandler(GenricWebsocketException.class)
@MessageExceptionHandler
public void handleCustomException(GenricWebsocketException ex) {
    GenericWebsocketResponse genericWebsocketExceptionResponse=
            new GenericWebsocketResponse(ex.getId(), ex.getDestination(), ex.getMessage(), ResponseStatus.error);
    simpMessagingTemplate.convertAndSendToUser(ex.getId(),"/"+ex.getDestination().toString(),genericWebsocketExceptionResponse);
}

}
