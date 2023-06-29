package com.indra.InQ.modal.common;

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
    private String startRange;
    private String endRange;
    private String maxInQueueLimit;
    private String description;
}
