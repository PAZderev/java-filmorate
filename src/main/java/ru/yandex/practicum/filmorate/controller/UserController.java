package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final LocalDate CURRENT_DAY = LocalDate.now();
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Adding new user: {}", user);
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User added: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Updating user: {}", user);
        if (user.getId() == null) {
            log.error("User id is null");
            throw new ValidationException("Id is required");
        }
        if (!users.containsKey(user.getId())) {
            log.error("User id not found");
            throw new ValidationException("User not found");
        }
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return user;
    }

    private long getNextId() {
        long nextId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
