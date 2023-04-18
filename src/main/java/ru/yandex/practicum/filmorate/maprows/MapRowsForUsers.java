package ru.yandex.practicum.filmorate.maprows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class MapRowsForUsers {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MapRowsForUsers(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("username"))
                .birthday(resultSet.getDate("birthday_date").toLocalDate())
                .friends(friendsFromDb(resultSet.getLong("user_id")))
                .build();
    }

    public User mapRowToNewUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("username"))
                .birthday(resultSet.getDate("birthday_date").toLocalDate())
                .build();
    }

    public Set<Long> friendsFromDb(Long id) {
        String sqlQuery = "SELECT user_id from friends WHERE user_friend_id =" + id;
        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class));
    }

}
