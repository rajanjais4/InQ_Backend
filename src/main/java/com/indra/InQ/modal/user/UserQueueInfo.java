package com.indra.InQ.modal.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueueInfo {
    @NonNull
    private String queueId;
    @NonNull
    private String queueName;
    @NonNull
    private UserStatus userStatus;
    @NonNull
    private Long CreationEpoch;
    @NonNull
    private Long userStatusChangeEpoch;
    private String category;
    private Integer noOfPeople;
}
