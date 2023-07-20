package com.indra.InQ.modal.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogInRequest {
    private Long phoneNumber;
    private String name;
}
