package com.indra.InQ.ws.response;

import com.indra.InQ.common.ResponseStatus;
import com.indra.InQ.exception.GenricWebsocketException;
import com.indra.InQ.modal.common.Destination;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.user.response.UserQueueUpdateResponse;
import com.indra.InQ.service.queueService.QueueService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WsUserResponseManager extends WsResponseManager{

    @Autowired
    private QueueService queueService;

    private void sendGenericResponseToUser(UserQueueUpdateResponse userQueueUpdateResponse){
        sendGenericResponse(userQueueUpdateResponse.getUserId(),
                Destination.userUpdate,
                userQueueUpdateResponse,
                ResponseStatus.success);
    }
    public UserQueueUpdateResponse createMandatoryUserResponse(QueueModal queueModal,
                                                               String userId,
                                                               Integer position){
        UserQueueUpdateResponse userQueueUpdateResponse =new UserQueueUpdateResponse();
        userQueueUpdateResponse.setUserId(userId);
        userQueueUpdateResponse.setQueueId(queueModal.getId());
        userQueueUpdateResponse.setQueuePosition(position);
        userQueueUpdateResponse.setQueueStatus(queueModal.getStatus());
        return userQueueUpdateResponse;
    }

//
    public void sendMandatoryResponseToAllUserInEntity(Entity entity){
        for (int i=0;i<entity.getQueueIds().size();i++){
            QueueModal queueModal=queueService.getQueueById(entity.getQueueIds().get(i));
            if(queueModal!=null){
                sendMandatoryResponseToAllUserInQueue(queueModal);
            }
        }
    }

    public void sendMandatoryResponseToAllUserInQueue(QueueModal queueModal){
        sendMandatoryResponseToUserInQueueRange(queueModal,
                0,
                queueModal.getUserInQueueList().size()-1);
        sendMandatoryResponseToAllUserWithQrGenerate(queueModal);
    }

    public void sendMandatoryResponseToUserInQueueRange(QueueModal queueModal,
                                                       Integer inQueueStartPosIndex,
                                                       Integer inQueueEndPosIndex){
        for(int i=inQueueStartPosIndex;i<=inQueueEndPosIndex;i++){
            UserQueueUpdateResponse userQueueUpdateResponse=
                    createMandatoryUserResponse(queueModal,
                            queueModal.getUserInQueueList().get(i),
                            i+1);
            sendGenericResponseToUser(userQueueUpdateResponse);
        }
    }
    public void sendMandatoryResponseToAllUserWithQrGenerate(QueueModal queueModal){
        try {
            for(int i=0;i<queueModal.getUserWithQrGenerated().size();i++){
                UserQueueUpdateResponse userQueueUpdateResponse=
                        createMandatoryUserResponse(queueModal,
                                queueModal.getUserWithQrGenerated().get(i),
                                i+1);
                sendGenericResponseToUser(userQueueUpdateResponse);
            }
        }
        catch (Exception e){
            System.out.println("In sendMandatoryResponseToUserInQueueRange: failed send response with error "+e.getMessage());
        }
    }
}
