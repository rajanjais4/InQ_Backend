package com.indra.InQ.modal.user.response;

import com.indra.InQ.modal.common.Status;
import com.indra.InQ.modal.user.UserQueueInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueueUpdateResponse {
    @NonNull
    private String userId;
    @NonNull
    private UserQueueInfo userQueueInfo;
    private Integer queueMovingRateInSeconds;
    @NonNull
    private Integer queuePosition;
    private Status queueStatus;
}
