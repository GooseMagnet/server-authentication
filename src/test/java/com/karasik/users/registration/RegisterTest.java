package com.karasik.users.registration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.karasik.exception.InvalidObjectException;
import com.karasik.model.UserDto;
import com.karasik.modules.AuthenticationModule;
import com.karasik.service.RegisterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private final String PASSWORD = "password";
    private String timestamp;

    private RegisterService registerService;
    private static Injector injector;

    @BeforeAll
    static void classSetup() {
        injector = Guice.createInjector(new AuthenticationModule());
    }

    @BeforeEach
    void createTimestamp() {
        timestamp = LocalDateTime.now().toString();
        registerService = injector.getInstance(RegisterService.class);
    }

    @Test
    void RegisterUser() {
        UserDto userDto = setUser();
        UserDto newUser = registerService.createUser(userDto);

        validateUserCreation(userDto, newUser);
    }

    @Test
    void RegisterUserExistingEmail() {
        // Register First User
        UserDto userDto = setUser();
        registerService.createUser(userDto);

        // Register User with same email
        UserDto repeatedUser = setUser();

        // Assert that Exception is thrown on creation
        assertThrows(InvalidObjectException.class, () -> registerService.createUser(repeatedUser));
    }

    @Test
    void RegisterUserPasswordMaxLength() {
        UserDto userDto = setUser();

        // Longest valid Password field
        userDto.setPassword("A".repeat(Byte.MAX_VALUE));
        UserDto newUser = registerService.createUser(userDto);
        validateUserCreation(userDto, newUser);
    }

    @Nested
    class RegisterUserInvalidPasswords {

        UserDto userDto = setUser();

        @Test
        void shortPassword() {
            UserDto userDto = setUser();

            // Password less than 8 Characters
            userDto.setPassword("pass");
            assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));
        }

        @Test
        void emptyPassword() {
            // Empty Password field
            userDto.setPassword("");
            assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));
        }

        @Test
        void nullPassword() {
            // Null Password field
            userDto.setPassword(null);
            assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));
        }

        @Test
        void longPassword() {
            // Password field too long
            userDto.setPassword("A".repeat(Byte.MAX_VALUE + 1));
            assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));
        }
    }

    @Test
    void RegisterUserInvalidEmail() {
        UserDto userDto = setUser();

        // Empty Email field
        userDto.setEmail("");
        assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));

        // Null Email field
        userDto.setEmail(null);
        assertThrows(InvalidObjectException.class, () -> registerService.createUser(userDto));
    }

    @Test
    void RegisterUserWithoutObject() {
        assertThrows(InvalidObjectException.class, () -> registerService.createUser(null));
    }

    private UserDto setUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("Email-" + timestamp);
        userDto.setName("Name-" + timestamp);
        userDto.setPassword(PASSWORD);
        return userDto;
    }

    private void validateUserCreation(UserDto preCreationUser, UserDto postCreationUser) {
        assertNotEquals(preCreationUser.getEmail(), postCreationUser.getEmail());
        assertEquals(preCreationUser.getEmail().toLowerCase(), postCreationUser.getEmail());
        assertEquals(preCreationUser.getName(), postCreationUser.getName());
        assertNotNull(postCreationUser.getPassword());
        assertNotNull(postCreationUser.getSalt());
        assertNotNull(postCreationUser.getId());
    }
}
