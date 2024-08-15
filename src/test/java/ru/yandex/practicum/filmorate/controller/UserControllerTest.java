package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.classes.inmemory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;
    private User validUser;

    @BeforeEach
    public void setUp() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        validUser = new User();
        validUser.setName("Valid Name");
        validUser.setLogin("ValidLogin");
        validUser.setEmail("ValidEmail@mail.ru");
        validUser.setBirthday(LocalDate.of(2021, 1, 1));
    }

    @Test
    public void testAddUser() {
        userController.addUser(validUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    public void updateUserTest() {
        userController.addUser(validUser);
        validUser.setName("Updated Name");
        userController.updateUser(validUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    public void notValidUpdateUserTest() {
        userController.addUser(validUser);
        User nonValidUser = new User();
        assertThrows(
                ValidationException.class,
                () -> userController.updateUser(nonValidUser)
        );
    }
}
