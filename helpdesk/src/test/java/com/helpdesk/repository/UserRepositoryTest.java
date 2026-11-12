package com.helpdesk.repository;

import com.helpdesk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Test
    void shouldSaveAndFindUser(){
        User user = new User("Christoffer", "secret", "mail@example.com", false);
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        User foundUser = result.orElseThrow();
        String name = foundUser.getUsername();

        assertEquals("Christoffer", name);
    }

}
