package ru.yandex.practicum.filmorate.maprows;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public final class MapRowsForUsers {

    public static User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("username"))
                .birthday(resultSet.getDate("birthday_date").toLocalDate())
                .build();
    }
}
