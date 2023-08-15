package com.indra.InQ.controller;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.entity.EntityQueueUpdateRequestWs;
import com.indra.InQ.modal.user.request.AddUserToQueueRequest;
import com.indra.InQ.modal.user.request.UserLogInRequest;
import com.indra.InQ.modal.user.request.UserQueueUpdateRequest;
import com.indra.InQ.modal.user.UserDb;
import com.indra.InQ.modal.user.response.UserQueueUpdateResponse;
import com.indra.InQ.service.userService.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    QueueControlWs queueControlWs;
    @PostMapping("/addUserToQueue")
    public ResponseEntity<UserQueueUpdateResponse> addUserToQueue(@RequestBody @NonNull AddUserToQueueRequest addUserToQueueRequest){
        UserQueueUpdateResponse userQueueUpdateResponse=
                userService.addUserToQueue(addUserToQueueRequest);

        EntityQueueUpdateRequestWs entityQueueUpdateRequestWs= new EntityQueueUpdateRequestWs();
        entityQueueUpdateRequestWs.setEntityId(addUserToQueueRequest.getEntityId());
        entityQueueUpdateRequestWs.setQueueId(userQueueUpdateResponse.getQueueId());
        queueControlWs.currentQueueSnapshot(entityQueueUpdateRequestWs);

        return ResponseEntity.ok(userQueueUpdateResponse);
    }
    @PostMapping("/loginUser")
    public ResponseEntity<UserDb> loginUser(@RequestBody @NonNull UserLogInRequest userLogInRequest){
        UserDb userDb=userService.getOrCreateUser(userLogInRequest);
        return ResponseEntity.ok(userDb);
    }
}
