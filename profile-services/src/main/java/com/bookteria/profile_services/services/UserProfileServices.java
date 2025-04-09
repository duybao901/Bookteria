package com.bookteria.profile_services.services;

import com.bookteria.profile_services.dto.request.UserProfileCreationRequest;
import com.bookteria.profile_services.dto.response.UserProfileResponse;
import com.bookteria.profile_services.entities.UserProfile;
import com.bookteria.profile_services.mapper.IUserProfileMapper;
import com.bookteria.profile_services.repositories.IUserProfileRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserProfileServices {
    private final IUserProfileRepository _userProfileRepository;
    private final IUserProfileMapper _userProfileMapper;

    public UserProfileServices(IUserProfileRepository userProfileRepository, IUserProfileMapper userProfileMapper) {
        this._userProfileRepository = userProfileRepository;
        this._userProfileMapper = userProfileMapper;
    }

    public UserProfileResponse createProfile(UserProfileCreationRequest userProfileCreationRequest){
        UserProfile userProfile = _userProfileMapper.toUserProfile(userProfileCreationRequest);
        userProfile = _userProfileRepository.save(userProfile);

        return _userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id){
        UserProfile userProfile = _userProfileRepository.findById(id).orElse(null);
        return _userProfileMapper.toUserProfileResponse(userProfile);
    }
}
