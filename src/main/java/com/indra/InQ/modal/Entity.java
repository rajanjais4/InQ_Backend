package com.indra.InQ.modal;

import com.indra.InQ.modal.common.Address;
import com.indra.InQ.modal.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Indexed()
    @NonNull
    private Long phoneNumber;
    @NonNull
    private String password;
    private Type type;
    private List<String> queueIds;
    private String summary;
    private Address address;
}
