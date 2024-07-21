package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.classes.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class UserDBTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.getUser(1);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    public void testFindAllUsers() {
        Collection<User> users = userStorage.getAllUsers();
        assertThat(users).isNotNull();
        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testDeleteUser() {
        userStorage.deleteUser(1);
        assertThrows(NotFoundException.class, () -> userStorage.getUser(1));
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setName("Test_Name");
        user.setLogin("Test_Login");
        user.setEmail("test@test.test");
        user.setBirthday(LocalDate.ofYearDay(2020,1));
        userStorage.addUser(user);
        assertThat(userStorage.getUser(user.getId())).isNotNull().isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        User user = userStorage.getUser(1);
        user.setName("Test_Name");
        userStorage.updateUser(user);
        assertThat(userStorage.getUser(user.getId())).isNotNull().hasFieldOrPropertyWithValue("name","Test_Name");

    }



}