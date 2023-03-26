package com.lucid.model;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

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
