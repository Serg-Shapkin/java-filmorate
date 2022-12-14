package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.exception.film.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final HashMap<Long, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // isBefore - раньше
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года {}", film.getName());
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года"); // 400
        } else if (film.getName() == null || film.getName().isBlank()) { // isBlank - пустой ли?
            log.error("Название фильма не может быть пустым. {}", film);
            throw new InvalidFilmNameException("Название фильма не может быть пустым.");
        } else if (films.containsKey(film.getId())) {
            log.error("Фильм {} был добавлен ранее", film.getName());
            throw new IncorrectFilmIdException(String.format("Фильм %s был добавлен ранее", film.getName()));
        } else {
            film.setId(filmId);
            films.put(film.getId(), film);
            filmId++;
            log.info("Фильм {} успешно добавлен", film.getName());
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // isBefore - раньше
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года {}", film.getName());
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года"); // 400
        } else if (!films.containsKey(film.getId())) {
            log.error("Фильм {} отсутствует в базе", film.getName());
            throw new IncorrectFilmIdException(String.format("Фильм %s отсутствует в базе", film.getName()));
        } else {
            films.put(film.getId(), film);
            log.info("Фильм {} успешно обновлен", film.getName());
            return film;
        }
    }

    @Override
    public Map<Long, Film> getAllFilms() {
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        if (!films.containsKey(id)){
            log.error("Фильм с id={} не найден в базе", id);
            throw new IncorrectFilmIdException(String.format("Фильм с id=%s не найден в базе", id));
        } else {
            log.info("Запрошен фильм c id={}", id);
            return films.get(id);
        }
    }
}