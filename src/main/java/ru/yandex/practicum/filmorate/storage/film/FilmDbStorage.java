package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.exception.film.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final static Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private int filmId = 0;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        makeFilmId();
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // isBefore - раньше
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года {}", film.getName());
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года"); // 400
        } else if (film.getName() == null || film.getName().isBlank()) { // isBlank - пустой ли?
            log.error("Название фильма не может быть пустым. {}", film);
            throw new InvalidFilmNameException("Название фильма не может быть пустым.");
        } else {
            filmId++;
            film.setId(filmId);
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
                    jdbcTemplate.update("INSERT INTO  GENRE_FILM(FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
                }
            }
            SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILM AS f INNER JOIN MPA AS m ON m.ID = f.RATING_ID WHERE FILM_ID = ?", film.getId());
            filmRowSet.next();
            return makeFilm(filmRowSet);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILM " +
                        "SET NAME = ?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION = ?, " +
                        "RATE = ?, " +
                        "RATING_ID = ? WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
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
    public List<Film> getAllFilms() {
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
    public Film getFilmById(Integer id) {
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILM AS f INNER JOIN MPA AS m ON m.ID = f.RATING_ID WHERE FILM_ID = ?", id);
        filmRowSet.next();
        if (filmRowSet.last()) {
            log.info("Запрошен фильм c id={}", id);
            return makeFilm(filmRowSet);
        } else {
            log.error("Указан некорректный id фильма");
            throw new IncorrectFilmIdException("Указан некорректный id фильма");
        }
    }

    @Override
    public void addLikeFilm(Integer id, Integer userId) {
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
        likeRowSet.next();

        if (!likeRowSet.last()) {
            jdbcTemplate.update("INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)", id, userId);
            jdbcTemplate.update("UPDATE FILM SET RATE = RATE + 1 WHERE FILM_ID = ?", id);
        }
    }

    @Override
    public void removeLikeFilm(Integer id, Integer userId) {
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
        likeRowSet.next();

        if (likeRowSet.last()) {
            jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, id);
            jdbcTemplate.update("UPDATE FILM SET RATE = RATE - 1 WHERE FILM_ID = ?", id);
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer size) {
        SqlRowSet popularFilmRowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID, RATE FROM FILM GROUP BY FILM_ID, RATE ORDER BY RATE DESC LIMIT ?", size);
        List<Film> films = new ArrayList<>();
        while (popularFilmRowSet.next()) {
            films.add(getFilmById(popularFilmRowSet.getInt("FILM_ID")));
        }
        return films;
    }

    private Film makeFilm(SqlRowSet filmRowSet) {
        Film film = new Film();
        int filmId = filmRowSet.getInt("FILM_ID");

        film.setId(filmId);
        film.setName(filmRowSet.getString("NAME"));
        film.setDescription(filmRowSet.getString("DESCRIPTION"));
        film.setReleaseDate(filmRowSet.getDate("RELEASE_DATE").toLocalDate());
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
        List<Genre> genres = new ArrayList<>();
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
        if (filmIdDb == null) {
            filmId = 0;
        } else {
            filmId = filmIdDb;
        }
    }
}
