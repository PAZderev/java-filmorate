package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelOperationException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean addNewFriend(long userId, long newFriendId) {
        log.info("User {} adding new friend: {} ", userId, newFriendId);
        User currentUser = userStorage.getUser(userId);
        User newFriendUser = userStorage.getUser(newFriendId);
        validateUsers(currentUser, newFriendUser);
        currentUser.getFriendList().add(newFriendId);
        newFriendUser.getFriendList().add(userId);
        log.info("User {} added new friend: {} ", userId, newFriendId);
        return true;
    }

    public boolean deleteFriend(long userId, long friendId) {
        log.info("User {} deleting friend: {} ", userId, friendId);
        User currentUser = userStorage.getUser(userId);
        User deleteFriend = userStorage.getUser(friendId);
        validateUsers(currentUser, deleteFriend);
        currentUser.getFriendList().remove(friendId);
        deleteFriend.getFriendList().remove(userId);
        log.info("User {} deleted friend: {} ", userId, friendId);
        return true;
    }

    public Collection<User> getFriends(long userId) {
        log.info("User {} getting friends", userId);
        User user = userStorage.getUser(userId);
        if (user == null) {
            log.error("User {} doesn't exist", userId);
            throw new NotFoundException("User not found");
        }
        return userStorage.getAllUsers()
                .stream()
                .filter(streamUser -> userStorage.getUser(user.getId()).getFriendList().contains(streamUser.getId()))
                .toList();
    }

    public Collection<User> getFriendCommon(long id, long otherId) {
        log.info("User {} getting friends common with friend {}", id, otherId);
        Set<Long> intersection = new HashSet<>(userStorage.getUser(id).getFriendList());
        intersection.retainAll(userStorage.getUser(otherId).getFriendList());

        return userStorage.getAllUsers().stream()
                .filter(user -> intersection.contains(user.getId()))
                .toList();
    }

    private void validateUsers(User one, User two) {
        log.debug("Validating users {} and {}", one, two);
        if (one == null || two == null) {
            log.error("Users {} or {} is null", one, two);
            throw new NotFoundException("User and NewFriend ids are required");
        }
        if (userStorage.getUser(one.getId()) == null || userStorage.getUser(two.getId()) == null) {
            log.error("Users {} or {} doesn't exist", one, two);
            throw new NotFoundException("User or NewFriend doesn't exist");
        }
        if (Objects.equals(one.getId(), two.getId())) {
            log.error("Users {} and {} is the same", one, two);
            throw new ModelOperationException("User and NewFriend ids must be unique");
        }
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUser(id);
    }

    public boolean deleteUser(long userId) {
        userStorage.deleteUser(userId);
        return true;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}
