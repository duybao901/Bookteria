package com.bookteria.profile_services.mapper;

import com.bookteria.profile_services.dto.request.UserProfileCreationRequest;
import com.bookteria.profile_services.dto.response.UserProfileResponse;
import com.bookteria.profile_services.entities.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest userProfileCreationRequest);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
