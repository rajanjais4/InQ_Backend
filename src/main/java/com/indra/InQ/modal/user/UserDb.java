package com.indra.InQ.modal.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "user")
public class UserDb {
    @Id
    private String userId;
    @NonNull
    private Long phoneNumber;
    @NonNull
    private String name;
    @NonNull
    private Long userCreationEpoch;
    @NonNull
    private List<UserQueueInfo> userInQueueInfoList;
    @NonNull
    private List<UserQueueInfo> userArchiveQueueInfoList;
}
