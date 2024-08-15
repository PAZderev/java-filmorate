package ru.yandex.practicum.filmorate.storage.classes.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(long id) {
        log.debug("Getting user with id {}", id);
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        log.info("Adding new user: {}", user);
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User added: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user: {}", user);
        if (user.getId() == null) {
            log.error("User id is null");
            throw new ValidationException("Id is required");
        }
        if (!users.containsKey(user.getId())) {
            log.error("User id not found");
            throw new NotFoundException("User not found");
        }
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public void deleteUser(long id) {
        log.info("Deleting user: {}", id);
        users.remove(id);
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
