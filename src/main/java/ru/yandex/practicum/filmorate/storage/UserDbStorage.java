package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Qualifier("dbStorageUser")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("username"))
                .birthday(resultSet.getDate("birthday_date").toLocalDate())
                .friends(friendsFromDb(resultSet.getLong("user_id")))
                .status_of_friendship(statusOfFriendships(resultSet.getLong("user_id")))
                .build();
    }

    private User mapRowToNewUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("username"))
                .birthday(resultSet.getDate("birthday_date").toLocalDate())
                .build();
    }

    private Set<Long> friendsFromDb(Long id) {
        String sqlQuery = "SELECT user_id from friends WHERE user_friend_id =" + id;
        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class));
    }

    private String statusOfFriendships(Long userId) {
        String sqlQuery = "SELECT status_code from friends where user_friend_id =" + userId;
        List<Long> frList = jdbcTemplate.queryForList(sqlQuery, Long.class);
        if (frList.contains(1L)) {
            return "not confirmed";
        } else {
            return "all confirmed";
        }
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUsers);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users(email, login, username, birthday_date)" +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        String sqlQueryTwo = "Select user_id, email, login, username, birthday_date from users where email = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToNewUsers, user.getEmail());
    }

    @Override
    public User updateUser(User user) throws EmptyResultDataAccessException {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, username = ?, birthday_date = ?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        String sqlQueryTwo = "Select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToNewUsers, user.getId());
    }

    @Override
    public User getUserById(Long id) throws EmptyResultDataAccessException {
        String sqlQuery = "select user_id, email, login, username, birthday_date, friends_id, status_of_friendship " +
                "from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUsers, id);
    }

    @Override
    public Collection<User> getFriendsById(long id) throws EmptyResultDataAccessException {
        String sqlQuery = "SELECT user_id from friends WHERE user_friend_id =" + id;
        List<Long> ids = jdbcTemplate.queryForList(sqlQuery, Long.class);
        Collection<User> friends = new ArrayList<>();
        for (Long i : ids) {
            friends.add(getUserById(i));
        }
        return friends;
    }

    @Override
    public User addFriends(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friends(user_friend_id, user_id, status_code)" +
                "values(?, ?, 1)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        String sqlQueryTwo = "Select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToUsers, userId);
    }

    @Override
    public User deleteFriends(Long userId, Long friendId) {
        String sqlQuery = "delete from friends where user_friend_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        String sqlQueryTwo = "Select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToUsers, userId);
    }

    @Override //Оставил под будущее, мне кажется это идеальный вариант запрашивать у БД, а не сравнивать в сервисе.
    public Collection<User> getClosedFriends(Long userId, Long friendId) {
        String sqlQuery = "SELECT u.name" +
                "FROM users u" +
                "JOIN friends f1 ON f1.user_friend_id = u.user_id" +
                "JOIN friends f2 ON f2.user_id = f1.user_id" +
                "JOIN users u2 ON u2.user_id = f2.user_friend_id" +
                "WHERE u.id =" + userId + " AND u2.id =" + friendId;
        Collection<Long> ids = jdbcTemplate.queryForList(sqlQuery, Long.class);
        Collection<User> users = new ArrayList<>();
        for (Long i : ids) {
            users.add(getUserById(i));
        }
        return users;
    }
}
