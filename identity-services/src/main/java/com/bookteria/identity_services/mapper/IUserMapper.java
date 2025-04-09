package com.bookteria.identity_services.mapper;

import com.bookteria.identity_services.dto.request.UserCreationRequest;
import com.bookteria.identity_services.dto.request.UserUpdateRequest;
import com.bookteria.identity_services.dto.response.UserResponse;
import com.bookteria.identity_services.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface IUserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
