package com.hospitalmanagement;


import com.hospitalmanagement.model.Role;
import com.hospitalmanagement.model.User;
import com.hospitalmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUserWithRole() {
        // GIVEN
        User user = User.builder()
                .username("admin")
                .email("admin@hopital.com")
                .password("1234")
                .roles(Set.of(Role.ADMIN))
                .build();

        // WHEN
        User savedUser = userRepository.save(user);

        // THEN
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getRoles()).contains(Role.ADMIN);
    }
}

