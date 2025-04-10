package com.bookteria.profile_services.controllers;

import com.bookteria.profile_services.dto.request.UserProfileCreationRequest;
import com.bookteria.profile_services.dto.response.UserProfileResponse;
import com.bookteria.profile_services.services.UserProfileServices;
import com.bookteria.shared.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class InternalUserProfileController {
    private final UserProfileServices _userProfileService;

    public InternalUserProfileController(UserProfileServices userProfileServices){
        this._userProfileService = userProfileServices;
    }

    @PostMapping("/internal/users")
    Result<UserProfileResponse> createProfile(@RequestBody UserProfileCreationRequest request) {
        return Result.success(_userProfileService.createProfile(request));
    }
}
