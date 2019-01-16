package de.htw.ai.wad.advi.service;

import de.htw.ai.wad.advi.model.User;
import de.htw.ai.wad.advi.repository.UserRepository;
import de.htw.ai.wad.advi.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    private UserService userServiceUnderTest;
    private User user;

    @Before
    public void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository,
                                               mockBCryptPasswordEncoder);
        user = User.builder()
                .id(1)
                .userId("Steve")
                .lastName("Bradley")
                .firstName("Steven")
                .isAdmin(true)
                .build();

        Mockito.when(mockUserRepository.save(any()))
                .thenReturn(user);
        Mockito.when(mockUserRepository.findByUserId(anyString()))
                .thenReturn(user);
    }

    @Test
    public void testFindUserByEmail() {
        System.out.println(new BCryptPasswordEncoder().encode("12345678"));
        // Setup
        final String userId = "Steve";

        // Run the test
        final User result = userServiceUnderTest.findUserByUserId(userId);

        // Verify the results
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testSaveUser() {
        // Setup
        final String userId = "Steve";

        // Run the test
        User test = User.builder().build();
        userServiceUnderTest.saveUser(User.builder().build());
    }
}