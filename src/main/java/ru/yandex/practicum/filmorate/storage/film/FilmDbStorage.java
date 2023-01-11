package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private int filmId = 0;
    private final JdbcTemplate jdbcTemplate;
    private final LikeStorage likeStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeStorage = likeStorage;
        makeFilmId();
    }

    @Override
    public Film add(Film film) {
        filmId++;
        film.setId(filmId);
        //film.setRate(0); // всегда при добавлении rate = 0
        jdbcTemplate.update("INSERT INTO FILM VALUES (?, ?, ?, ?, ?, ?, ?)",
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRate(),
                    film.getMpa().getId());

        if (film.getGenres() != null) {
            Set<Genre> genres = Set.copyOf(film.getGenres());
            for (Genre genre : genres) {
                jdbcTemplate.update("INSERT INTO GENRE_FILM(FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
            }
        }
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILM AS f INNER JOIN MPA AS m ON m.ID = f.RATING_ID WHERE FILM_ID = ?", film.getId());
        filmRowSet.next();
        return makeFilm(filmRowSet);
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE FILM " +
                        "SET NAME = ?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION = ?, " +
                        //"RATE = ?, " +
                        "RATING_ID = ? WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                //film.getRate(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            jdbcTemplate.update("DELETE FROM GENRE_FILM WHERE FILM_ID = ?", film.getId());
            Set<Genre> genres = Set.copyOf(film.getGenres());
            for (Genre genre : genres) {
                jdbcTemplate.update("INSERT INTO GENRE_FILM(FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
            }
        }
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM FILM AS f " +
                        "INNER JOIN MPA AS m ON m.ID = f.RATING_ID " +
                        "WHERE FILM_ID = ?", film.getId());
        if (filmRowSet.next()) {
            log.info("Фильм {} успешно обновлен", film.getName());
            return makeFilm(filmRowSet);
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getAll() {
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILM AS f INNER JOIN MPA AS m ON m.ID = f.RATING_ID");
        List<Film> films = new ArrayList<>();
        while (filmRowSet.next()) {
            films.add(makeFilm(filmRowSet));
        }
        if (films.isEmpty()) {
            log.error("В базе не сохранено ни одного фильма");
        }
        log.info("Запрошены все фильмы из базы данных");
        return films;
    }

    @Override
    public Film getById(Integer id) {
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILM AS f INNER JOIN MPA AS m ON m.ID = f.RATING_ID WHERE FILM_ID = ?", id);
        filmRowSet.next();
        if (filmRowSet.last()) {
            // log.info("Запрошен фильм c id={}", id);
            return makeFilm(filmRowSet);
        } else {
            log.error("Указан некорректный id фильма");
            throw new IncorrectFilmIdException("Указан некорректный id фильма");
        }
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
    }

    @Override
    public List<Film> getPopular(Integer size) {
        SqlRowSet popularFilmRowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID, RATE FROM FILM GROUP BY FILM_ID, RATE ORDER BY RATE DESC LIMIT ?", size);
        List<Film> films = new ArrayList<>();
        while (popularFilmRowSet.next()) {
            films.add(getById(popularFilmRowSet.getInt("FILM_ID")));
        }
        return films;
    }

    private Film makeFilm(SqlRowSet filmRowSet) {
        Film film = new Film();
        int filmId = filmRowSet.getInt("FILM_ID");

        film.setId(filmId);
        film.setName(filmRowSet.getString("NAME"));
        film.setDescription(filmRowSet.getString("DESCRIPTION"));
        film.setReleaseDate(Objects.requireNonNull(filmRowSet.getDate("RELEASE_DATE")).toLocalDate());
        film.setDuration(filmRowSet.getInt("DURATION"));
        film.setRate(filmRowSet.getInt("RATE"));

        // рейтинг
        film.setMpa(new Rating(filmRowSet.getInt("RATING_ID"), filmRowSet.getString("RATING")));

        // лайки
        Set<Integer> like = new HashSet<>();
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES WHERE FILM_ID = ?", filmId);
        while (likeRowSet.next()) {
            like.add(likeRowSet.getInt("USER_ID"));
        }
        film.setLikes(like);

        // жанры
        Set<Genre> genres = new LinkedHashSet<>();
        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet("SELECT g.ID, g.GENRE " +
                "FROM GENRE AS g " +
                "INNER JOIN GENRE_FILM AS gf ON g.ID = gf.GENRE_ID " +
                "WHERE FILM_ID = ? " +
                "GROUP BY g.ID, g.GENRE " +
                "ORDER BY g.ID", filmId);
        while (genreRowSet.next()) {
            genres.add(new Genre(genreRowSet.getInt("ID"), genreRowSet.getString("GENRE")));
        }
        film.setGenres(genres);
        return film;
    }

    private void makeFilmId() { // если в бд есть фильмы, то запоминаем максимальный id
        Integer filmIdDb = jdbcTemplate.queryForObject("SELECT MAX(FILM_ID) FROM FILM", Integer.class);
        filmId = Objects.requireNonNullElse(filmIdDb, 0);
    }
}
