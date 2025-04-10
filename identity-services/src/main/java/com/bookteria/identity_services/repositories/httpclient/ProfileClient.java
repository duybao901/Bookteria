package com.bookteria.identity_services.repositories.httpclient;

import com.bookteria.identity_services.dto.request.user_profile.UserProfileCreationRequest;
import com.bookteria.identity_services.dto.response.user_profile.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-services", url = "${app.services.profile}")
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request);
}
