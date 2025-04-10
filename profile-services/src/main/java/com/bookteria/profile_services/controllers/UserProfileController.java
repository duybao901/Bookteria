package com.bookteria.profile_services.controllers;

import com.bookteria.profile_services.dto.request.UserProfileCreationRequest;
import com.bookteria.profile_services.dto.response.UserProfileResponse;
import com.bookteria.profile_services.services.UserProfileServices;
import com.bookteria.shared.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
    private final UserProfileServices _userProfileService;

    public UserProfileController(UserProfileServices userProfileServices){
        this._userProfileService = userProfileServices;
    }

    @GetMapping("/users/{profileId}")
    Result<UserProfileResponse> getProfile(@PathVariable String profileId){
        return Result.success(_userProfileService.getProfile(profileId));
    }
}
