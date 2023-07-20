package com.indra.InQ.modal.entity;

import com.indra.InQ.modal.common.Address;
import com.indra.InQ.modal.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "entity")
public class Entity {
    @Id
    private String id;
    @NonNull
    @Indexed()
    private String name;
    private String email;
    @Indexed(unique = true)
    @NonNull
    private Long phoneNumber;
    @NonNull
    private String password;
    @NonNull
    private Status status;
    private List<String> categories;
    @NonNull
    private EntityType entityType;
    private List<String> queueIds=new ArrayList<>();
    private String summary;
    private Address address;
}
