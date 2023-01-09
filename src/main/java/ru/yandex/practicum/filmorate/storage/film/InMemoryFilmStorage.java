package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.film.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.exception.film.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmLikesComparator;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final UserService userService;
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    public InMemoryFilmStorage(UserService userService) {
        this.userService = userService;
    }

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
    public List<Film> getAllFilms() {
        if (films.isEmpty()) {
            log.error("В базе не сохранено ни одного фильма");
        }
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (!films.containsKey(id)){
            log.error("Фильм с id={} не найден в базе", id);
            throw new IncorrectFilmIdException(String.format("Фильм с id=%s не найден в базе", id));
        } else {
            log.info("Запрошен фильм c id={}", id);
            return films.get(id);
        }
    }

    @Override
    public void addLikeFilm(Integer id, Integer userId) {
        User user = userService.getUserById(userId);

        filmValidation(id);
        Film film = getFilmById(id);
        film.getLikes().add(user.getId());

        films.put(film.getId(), film);
        log.info("Пользователь {} поставил лайк фильму {}", user.getName(), film.getName());
    }

    @Override
    public void removeLikeFilm(Integer id, Integer userId) {
        User user = userService.getUserById(userId);

        filmValidation(id);
        Film film = getFilmById(id);
        film.getLikes().remove(user.getId());

        films.put(film.getId(), film);
        log.info("Пользователь {} удалил лайк у фильма {}", user.getName(), film.getName());
    }

    @Override
    public List<Film> getPopularFilms(Integer size) {
        List<Film> films = getAllFilms();

        FilmLikesComparator comparator = new FilmLikesComparator();
        films.sort(comparator);

        log.info("Запрошен список из {} фильмов",size);

        return films.stream()
                .limit(size)
                .collect(Collectors.toList());
    }

    private void filmValidation(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmValidationException("Фильм с id=" + id + " не найден в базе");
        }
    }
}