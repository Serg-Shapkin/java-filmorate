package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer id, Integer userId) {
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
        likeRowSet.next();

        if (!likeRowSet.last()) {
            jdbcTemplate.update("INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)", id, userId);
            jdbcTemplate.update("UPDATE FILM SET RATE = RATE + 1 WHERE FILM_ID = ?", id);
        }

        updateRate(id);
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
        likeRowSet.next();

        if (likeRowSet.last()) {
            jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
            jdbcTemplate.update("UPDATE FILM SET RATE = RATE - 1 WHERE FILM_ID = ?", id);
        }

        updateRate(id);
    }
    private void updateRate(Integer filmId) {
        String sqlQuery = "UPDATE FILM f SET rate = (SELECT count(l.USER_ID) from LIKES l where l.FILM_ID = f.FILM_ID) WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
