package com.helpdesk.config;

import com.helpdesk.model.Role;
import com.helpdesk.model.User;
import com.helpdesk.repository.UserRepository;
import com.helpdesk.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    public DevDataInitializer(UserService userService, UserRepository userRepository) {
        this.userService =  userService;
        this.userRepository = userRepository;

    }
    @Override
    public void run(String... args) throws Exception {

        Optional<User> result = userRepository.findByUsername("devuser");
        if (result.isEmpty()) {
            User user = new User("devuser", "secret", "devuser@example.com", true, Role.USER);
            userService.createUser(user);
        }
    }
}
