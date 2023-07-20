package com.indra.InQ.service.userService;

import com.indra.InQ.common.Common;
import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.entity.Entity;
import com.indra.InQ.modal.queue.QueueModal;
import com.indra.InQ.modal.user.*;
import com.indra.InQ.modal.user.request.AddUserToQueueRequest;
import com.indra.InQ.modal.user.request.UserLogInRequest;
import com.indra.InQ.modal.user.response.UserQueueUpdateResponse;
import com.indra.InQ.repository.UserRepo;
import com.indra.InQ.service.EntityService;
import com.indra.InQ.service.queueService.QueueService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    QueueService queueService;
    @Autowired
    EntityService entityService;
    @Autowired
    Common common;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserQueueMatchService userQueueMatchService;

    public UserDb findUserByPhoneNumber(Long phoneNumber)
    {
        UserDb userDb = userRepo.findByPhoneNumber(phoneNumber).orElse(null);
        if(userDb==null)
            System.out.println("UserService.findUserByPhoneNumber - no entity found - "+phoneNumber);
        else
            System.out.println("UserService.findUserByPhoneNumber entity found - "+phoneNumber);
        return userDb;
    }
    public UserDb findUserByUserId(String userId)
    {
        UserDb userDb = userRepo.findById(userId).orElse(null);
        if(userDb==null)
            System.out.println("UserService.findUserByUserId - no entity found - "+userId);
        else
            System.out.println("UserService.findUserByUserId entity found - "+userId);
        return userDb;
    }
    private UserDb createEmptyByPhoneNumberUserDb(UserLogInRequest userLogInRequest) {
        UserDb userDb=new UserDb();

        userDb.setPhoneNumber(userLogInRequest.getPhoneNumber());
        userDb.setUserId(common.createUserId(userLogInRequest.getPhoneNumber()));
        userDb.setName(userLogInRequest.getName());
        userDb.setUserInQueueInfoList(new ArrayList<>());
        userDb.setUserArchiveQueueInfoList(new ArrayList<>());
        Long currentTimeEpoch= Instant.now().getEpochSecond();
        userDb.setUserCreationEpoch(currentTimeEpoch);

        return userDb;
    }
    private UserQueueInfo createUserQueueInfo(AddUserToQueueRequest addUserToQueueRequest) {
        Entity entity=entityService.findUserByEntityId(addUserToQueueRequest.getEntityId());
        if(entity==null) throw new ApiRequestException("Entity ID does not exists");
        QueueModal queueModal = userQueueMatchService.getBestQueueMatchForUser(entity, addUserToQueueRequest);
        Long currentTimeEpoch= Instant.now().getEpochSecond();
        UserQueueInfo userQueueInfo=new UserQueueInfo(queueModal.getId(),
                queueModal.getName(),
                UserStatus.inQueue,
                currentTimeEpoch,
                currentTimeEpoch,
                addUserToQueueRequest.getCategory(),
                addUserToQueueRequest.getNoOfPeople());
        return userQueueInfo;
    }

    public void addOrUpdateUser(UserDb userDb){
        try {
            userRepo.save(userDb);
        }
        catch (Exception e){
            throw new ApiRequestException("error while adding or updating user");
        }
    }
    public UserQueueUpdateResponse addUserToQueue(@NonNull AddUserToQueueRequest addUserToQueueRequest) {
        UserDb userDb=findUserByUserId(addUserToQueueRequest.getUserId());
        if(userDb==null)    throw new ApiRequestException("User not found - "+addUserToQueueRequest.getUserId());
        UserQueueInfo userQueueInfo=
                createUserQueueInfo(addUserToQueueRequest);
        UserQueueUpdateResponse userQueueUpdateResponse=queueService.updateQueueByUserAction(userQueueInfo,userDb.getUserId(), UserAction.add);
        userQueueInfo=userQueueUpdateResponse.getUserQueueInfo();
        userDb.getUserInQueueInfoList().add(userQueueInfo);
        addOrUpdateUser(userDb);
        return userQueueUpdateResponse;
    }


    public UserDb getOrCreateUser(@NonNull UserLogInRequest userLogInRequest) {
        UserDb userDb=findUserByPhoneNumber(userLogInRequest.getPhoneNumber());
        if(userDb==null)    userDb=createEmptyByPhoneNumberUserDb(userLogInRequest);
        addOrUpdateUser(userDb);
        return userDb;
    }
}
