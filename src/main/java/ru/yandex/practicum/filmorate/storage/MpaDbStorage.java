package ru.yandex.practicum.filmorate.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.maprows.MapRowsForMpa;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MapRowsForMpa mapRowsForMpa;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, MapRowsForMpa mapRowsForMpa) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowsForMpa = mapRowsForMpa;
    }

    @Override
    public Collection<Mpa> findAll() {
        String sqlQuery = "select mpa_id, name from mpa_rating";
        return jdbcTemplate.query(sqlQuery, mapRowsForMpa::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(Long id) {
        String sqlQuery = "select mpa_id, name from mpa_rating where mpa_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, mapRowsForMpa::mapRowToMpa, id);
    }
}
