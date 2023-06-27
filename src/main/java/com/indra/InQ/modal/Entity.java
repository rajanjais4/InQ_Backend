package com.indra.InQ.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indra.InQ.modal.common.Address;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.modal.common.Type;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "entity")
public class Entity {
    @Id
    private String _id;
    @NonNull
    @Indexed()
    private String name;
    private String email;
    @Indexed()
    @NonNull
    private String phoneNumber;
    @NonNull
    private String password;
    private Type type;
    private List<QueueDescription> queueDescriptions;
    private String summary;
    private Address address;
}
