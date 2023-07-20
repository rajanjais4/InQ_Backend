package com.indra.InQ.modal.user.request;

import com.indra.InQ.modal.user.UserAction;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserQueueUpdateRequest {
    @NonNull
    private String userId;
    private UserAction userAction;
    private String moveBackDelay;
    @NonNull
    private String queueId;

}
