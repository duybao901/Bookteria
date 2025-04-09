package com.bookteria.identity_services.dto.response;

import com.bookteria.identity_services.entities.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
//    String password;
    LocalDate birthday;

    Set<Role> roles;
}
