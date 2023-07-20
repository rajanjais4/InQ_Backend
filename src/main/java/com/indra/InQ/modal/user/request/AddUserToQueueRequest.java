package com.indra.InQ.modal.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToQueueRequest {
    @NonNull
    private String userId;
    @NonNull
    private String category;
    private Integer noOfPeople;
    @NonNull
    private String entityId;
}
