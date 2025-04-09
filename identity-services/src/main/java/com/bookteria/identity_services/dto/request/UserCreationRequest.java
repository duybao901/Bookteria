package com.bookteria.identity_services.dto.request;

import com.bookteria.identity_services.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 6, message = "Username must be at least {min} character")
    String username;

    @Size(min = 6, message = "Password must be at least {min} character")
    String password;

    @DobConstraint(minAge = 20, message = "your age must be at least {minAge}")
    LocalDate birthday;
}
