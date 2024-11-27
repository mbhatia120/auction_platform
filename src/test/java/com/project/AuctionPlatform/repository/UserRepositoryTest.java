package com.project.AuctionPlatform.repository;


import com.project.AuctionPlatform.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest // This annotation sets up an in-memory database for repository testing
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsernameOrEmail() {
        // Arrange: Save a test user in the in-memory database
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);

        // Act: Search for the user by username or email
        Optional<User> found = userRepository.findByUsernameOrEmail("testuser", "test@example.com");

        // Assert: Verify that the user was found
        assertTrue(found.isPresent());
    }
}
