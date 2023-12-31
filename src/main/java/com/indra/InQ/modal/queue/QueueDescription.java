package com.indra.InQ.modal.queue;

import com.indra.InQ.modal.common.Status;
import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueueDescription {
    @Id
    private String id;
    @NonNull
    private String name;
    private Integer queueSize;
    @NonNull
    private Status status;
    private Integer startRange;
    private Integer endRange;
    private Integer maxInQueueLimit;
    private String description;
    private String category;
}
