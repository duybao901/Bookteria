package com.bookteria.identity_services.dto.request;

import com.bookteria.identity_services.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;

    @DobConstraint(minAge = 2)
    LocalDate birthday;
}
