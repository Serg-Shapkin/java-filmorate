package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")  // http://localhost:8080/films
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    // добавление фильма - POST
    @PostMapping
    public Film addFilm (@RequestBody Film film) {
        if (films.containsValue(film)) {
            log.error("Фильм \"{}\" был добавлен ранее.", film.getName());
            throw new CheckingDuplicateFilmException("Фильм с названием \"" + film.getName() + "\" был добавлен ранее.");
        } else if (film.getName() == null || film.getName().isBlank()) { // isBlank - пустой ли?
            log.error("Название фильма не может быть пустым. {}", film);
            throw new InvalidFilmNameException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            log.error("Описание фильма не может быть более 200 символов. {}", film);
            throw new InvalidDescriptionFilmException("Описание фильма не может быть более 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // isBefore - раньше
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года. {}", film);
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной. {}", film);
            throw new InvalidDurationFilmException("Продолжительность фильма должна быть положительной.");
        } else {
            film.setId(filmId);
            films.put(filmId, film);
            filmId++;
            log.info("Фильм \"{}\" c id \"{}\" успешно добавлен.", film.getName(), film.getId());
            System.out.println("Фильм \"" + film.getName() + "\" c id \"" + film.getId() + "\" успешно добавлен.");
            return film;
        }
    }

    // обновление фильма - PUT
    @PutMapping
    public Film addOrUpdateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Невозможно обновить данные о фильме. Фильм отсутствует в базе. {}", film);
            throw new InvalidFilmException("Невозможно обновить данные о фильме. Фильм отсутствует в базе.");
        } else {
            films.put(film.getId(), film);
            log.info("Фильм c id \"{}\" успешно обновлен.", film.getId());
            System.out.println("Фильм c id \"" + film.getId() + "\" успешно обновлен.");
            return film;
        }
    }

    // получение всех фильмов - GET
    @GetMapping
    public List<Film> getFilms() {
        System.out.println("Получен список всех фильмов.");
        return new ArrayList<>(films.values());
    }
}
