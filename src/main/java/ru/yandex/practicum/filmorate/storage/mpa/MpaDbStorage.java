package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Rating> getAllRating() {
        SqlRowSet ratingRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA");
        List<Rating> ratings = new ArrayList<>();
        while (ratingRowSet.next()) {
            ratings.add(makeRating(ratingRowSet));
        }
        return ratings;
    }

    @Override
    public Rating getRatingById(Integer id) {
        SqlRowSet ratingRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE ID = ?", id);
        ratingRowSet.next();
        if (ratingRowSet.last()) {
            return makeRating(ratingRowSet);
        } else {
            return null;
        }
    }

    private Rating makeRating(SqlRowSet ratingRowSet) {
        Rating rating = new Rating();
        rating.setId(ratingRowSet.getInt("ID"));
        rating.setName(ratingRowSet.getString("RATING"));
        return rating;
    }
}
