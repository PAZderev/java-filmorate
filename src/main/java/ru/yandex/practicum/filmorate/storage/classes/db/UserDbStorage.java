package ru.yandex.practicum.filmorate.storage.classes.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Repository
@Qualifier
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * from users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_SENT_FRIEND_REQUESTS_QUERY = "SELECT user2_id FROM user_friends WHERE user1_id = ?";
    private static final String FIND_RECEIVED_FRIEND_REQUESTS_QUERY = "SELECT user1_id FROM user_friends WHERE user2_id = ?";

    private static final String INSERT_USER_DATA_QUERY = "INSERT INTO users " +
            "(email," +
            " name," +
            " login," +
            " birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String INSERT_USER_FRIEND_REQUEST_QUERY = "INSERT INTO user_friends" +
            " (user1_id, user2_id) VALUES (?, ?)";

    private static final String UPDATE_USER_QUERY = "UPDATE users SET " +
            "email = ?, name = ?, login = ?, birthday = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String DELETE_USER_FRIEND_REQUEST_QUERY = "DELETE FROM user_friends" +
            " WHERE user1_id = ? and user2_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, PlatformTransactionManager transactionManager) {
        super(jdbc, mapper, transactionManager);
    }

    @Override
    public User getUser(long id) {
        Optional<User> optionalUser = findOne(FIND_BY_ID_QUERY, id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        User user = optionalUser.get();
        fillFriendRequests(user);
        return user;
    }

    @Override
    public User addUser(User user) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            long userId = insert(INSERT_USER_DATA_QUERY, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
            user.setId(userId);
            for (Long id : user.getSentFriendRequests()) {
                update(INSERT_USER_FRIEND_REQUEST_QUERY, user.getId(), id);
            }
            for (Long id : user.getReceivedFriendRequests()) {
                update(INSERT_USER_FRIEND_REQUEST_QUERY, id, user.getId());
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new InternalServerException("Не удалось добавить пользователя " + user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        long totalRowsUpdated = 0;
        User currentUser = getUser(user.getId());
        if (currentUser == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            totalRowsUpdated += updateUserData(user);
            totalRowsUpdated += updateFriendRequests(user);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new InternalServerException("При обновлении пользователя " + user + " возникла ошибка" + e.getMessage());
        }
        if (totalRowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return user;
    }

    private long updateUserData(User user) {
        return update(UPDATE_USER_QUERY,
                user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
    }

    private long updateFriendRequests(User user) {
        long rowsUpdated = 0;
        // Обновление исходящих заявок
        Set<Long> currentSentRequests = new HashSet<>(jdbc.queryForList(FIND_SENT_FRIEND_REQUESTS_QUERY, Long.class, user.getId()));
        Set<Long> newSentRequests = user.getSentFriendRequests();

        for (Long id : newSentRequests) {
            if (!currentSentRequests.contains(id)) {
                rowsUpdated += update(INSERT_USER_FRIEND_REQUEST_QUERY, user.getId(), id);
            }
        }

        for (Long id : currentSentRequests) {
            if (!newSentRequests.contains(id)) {
                rowsUpdated += delete(DELETE_USER_FRIEND_REQUEST_QUERY, user.getId(), id);
            }
        }

        // Обновление входящих заявок
        Set<Long> currentReceivedRequests = new HashSet<>(jdbc.queryForList(FIND_RECEIVED_FRIEND_REQUESTS_QUERY, Long.class, user.getId()));
        Set<Long> newReceivedRequests = user.getReceivedFriendRequests();

        for (Long id : newReceivedRequests) {
            if (!currentReceivedRequests.contains(id)) {
                rowsUpdated += update(INSERT_USER_FRIEND_REQUEST_QUERY, id, user.getId());
            }
        }

        for (Long id : currentReceivedRequests) {
            if (!newReceivedRequests.contains(id)) {
                rowsUpdated += delete(DELETE_USER_FRIEND_REQUEST_QUERY, id, user.getId());
            }
        }

        return rowsUpdated;
    }


    @Override
    public Collection<User> getAllUsers() {
        List<User> users = findMany(FIND_ALL_QUERY);
        for (User user : users) {
            fillFriendRequests(user);
        }
        return users;
    }

    @Override
    public void deleteUser(long id) {
        delete(DELETE_USER_QUERY, id);
    }

    private void fillFriendRequests(User user) {
        user.setSentFriendRequests(new HashSet<>(jdbc.queryForList(FIND_SENT_FRIEND_REQUESTS_QUERY, Long.class,
                user.getId())));
        user.setReceivedFriendRequests(new HashSet<>(jdbc.queryForList(FIND_RECEIVED_FRIEND_REQUESTS_QUERY, Long.class,
                user.getId())));
    }
}
