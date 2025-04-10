package com.bookteria.identity_services.mapper;

import com.bookteria.identity_services.dto.request.UserCreationRequest;
import com.bookteria.identity_services.dto.request.user_profile.UserProfileCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserProfileMapper {
    UserProfileCreationRequest toUserProfileCreationRequest(UserCreationRequest request);

//    UserProfileResponse toUserProfileResponse(User)
}
