package com.lucid.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String mailId;
    private String firstName;
    private String lastName;
    private UserStatus status = UserStatus.ACTIVE;
}
