package com.queue.queue_client.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "entity")
@AllArgsConstructor
public class Entity {

    @Id
    private int id;
    private String name;
    private String phoneNo;
    private String password;
}
