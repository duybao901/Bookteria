package com.bookteria.identity_services.configurations;

import com.bookteria.identity_services.entities.Role;
import com.bookteria.identity_services.entities.User;
import com.bookteria.identity_services.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role role = new Role();
                role.setName("ADMIN");

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setBirthday(LocalDate.parse("1989-02-03"));

                Set<Role> roles = new HashSet<>();
                roles.add(role);

                user.setRoles(roles);
                userRepository.save(user); // Save your entity to the database
            }
        };
    }
}
