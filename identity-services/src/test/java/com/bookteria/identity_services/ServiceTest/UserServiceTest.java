package com.bookteria.identity_services.ServiceTest;

import com.bookteria.identity_services.dto.request.UserCreationRequest;
import com.bookteria.identity_services.dto.response.UserResponse;
import com.bookteria.identity_services.entities.User;
import com.bookteria.identity_services.exceptions.UserException;
import com.bookteria.identity_services.repositories.IUserRepository;
import com.bookteria.identity_services.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@Import(UserService.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private IUserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;
    private User user;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("johnreacher")
                .password("12345678")
                .birthday(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("johnreacher")
                .birthday(dob)
                .build();

        user = User.builder()
                .id("cf0600f538b3")
                .username("john")
                .birthday(dob)
                .build();
    }

    @Test
    void createUser_ValidRequest_Success() {
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
        Assertions.assertThat(response.getBirthday()).isEqualTo("1990-01-01");
    }

    @Test
    void createUser_UserExisted_Fail() {
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // WHEN
        var exception = assertThrows(UserException.UserWithUserNameAlreadyExists.class, () -> {
            userService.createUser(request);
        });

        // THEN
        Assertions.assertThat(exception.getMessage()).isEqualTo("User with username 'johnreacher' already exists");
        Assertions.assertThat(exception.getTitle()).isEqualTo("Bad Request");
    }
}
