package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.exception.user.InvalidIdUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);
    private int filmId = 1;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {

        if (filmStorage.getFilms().containsValue(film)) {
            log.error("Фильм {} был добавлен ранее.", film.getName());
            throw new DuplicateFilmException(String.format("Фильм %s был добавлен ранее.", film.getName()));

        } else if (film.getName() == null || film.getName().isBlank()) { // isBlank - пустой ли?
            log.error("Название фильма не может быть пустым.");
            throw new InvalidFilmNameException("Название фильма не может быть пустым.");

        } else if (film.getDescription().length() > 200) {
            log.error("Описание фильма не может быть более 200 символов. {}", film.getName());
            throw new InvalidDescriptionFilmException("Описание фильма не может быть более 200 символов.");

        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // isBefore - раньше
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года. {}", film.getName());
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");

        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной. {}", film.getName());
            throw new InvalidDurationFilmException("Продолжительность фильма должна быть положительной.");

        } else {
            film.setId(filmId);
            filmStorage.addFilm(film);
            filmId++;
            log.info("Фильм {} успешно добавлен.", film.getName());
            return film;
        }
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.getFilms().containsKey(film.getId())) {
            log.error("Невозможно обновить данные о фильме. Фильм отсутствует в базе.");
            throw new InvalidFilmException("Невозможно обновить данные о фильме. Фильм отсутствует в базе.");
        } else {
            filmStorage.updateFilm(film);
            log.info("Фильм {} успешно обновлен.", film.getName());
            return film;
        }
    }

    public List<Film> getAllFilms() {
        if (filmStorage.getAllFilms() == null || filmStorage.getAllFilms().isEmpty()) {
            log.error("В базе не сохранено ни одного фильма.");
            throw new FilmDatabaseIsEmptyException("В базе не сохранено ни одного фильма.");
        }
        log.info("Запрошен список всех фильмов.");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        if (filmStorage.getFilmById(id) == null) {
            log.error("Указан некорректныйй id фильма.");
            throw new InvalidIdFilmException("Указан некорректный id фильма.");
        }
        log.info("запрошен фильм c id {}.", id);
        return filmStorage.getFilmById(id);
    }

    public Film addLikeFilm(long id, long userId) { // id - какому фильму, userId - кто ставит
        Film film = filmStorage.getFilmById(id);    // получили фильм которому хотим поставить лайк
        if (film == null) {
            log.error("Указан некорректныйй id фильма.");
            throw new InvalidIdFilmException("Указан некорректный id фильма.");
        }

        if (film.getLikes() == null) {              // если списка лайков еще нет (только запущена программа и лайк никто не ставил)
            film.setLikes(new HashSet<>());         // то создадем список для лайков
        }

        User user = userStorage.getUserById(userId); // получили пользователя по id
        if (user == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректныйй id пользователя.");
        }

        filmStorage.addLikeFilm(id, userId);
        log.info("Пользователь {} поставил лайк фильму {}.", user.getName(), film.getName());

        return film;
    }

    public Film removeLikeFilm(long id, long userId) { // id - из какого фильма, userId - кто удаляет
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.error("Указан некорректныйй id фильма.");
            throw new InvalidIdFilmException("Указан некорректный id фильма.");
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректныйй id пользователя.");
        }

        if (film.getLikes() == null || film.getLikes().isEmpty()) {  // если Set null или пустой
            log.error("У фильма {} еще нет лайков.", film.getName());
            throw new InvalidCountLikesFilmException(String.format("У фильма %s еще нет лайков", film.getName()));

        } else if (!film.getLikes().contains(userId)) {              // если Set не соджержит userId
            log.error("Лайк невезможно удалить, так как пользователь {} его еще не поставил.", user.getName());
            throw new InvalidRemoveLikeFilmException(String.format(
                    "Лайк невезможно удалить, так как пользователь %s его еще не поставил.", user.getName()));
        } else {
            filmStorage.removeLikeFilm(id, userId);
            log.info("Пользователь {} удалил лайк у фильма {}.", user.getName(), film.getName());
        }

        return film;
    }

    public List<Film> getPopularFilms(Long size) {
        if (filmStorage.getAllFilms() == null || filmStorage.getAllFilms().isEmpty()) {
            log.error("В базе не сохранено ни одного фильма.");
            throw new FilmDatabaseIsEmptyException("В базе не сохранено ни одного фильма.");
        } else {
            log.info("Запрошен список из {} фильмов.",size);
            return filmStorage.getAllFilms().stream() // ура, мой второй стрим!
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(size)
                    .collect(Collectors.toList());
        }
    }
}