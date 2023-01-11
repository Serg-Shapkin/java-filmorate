package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateFilmException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private static final LocalDate RELEASE_DATE = LocalDate.of(1895,12,28);
    private final FilmDbStorage filmDbStorage;
    private final UserService userService;

    @Override
    public Film add(Film film) {
        filmReleaseDateValidation(film); // проверка даты релиза
        return filmDbStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        filmReleaseDateValidation(film); // проверка даты релиза
        filmValidation(film.getId());
        return filmDbStorage.update(film);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmDbStorage.getAll());
    }

    @Override
    public Film getById(Integer id) {
        filmValidation(id);
        return filmDbStorage.getById(id);
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        filmValidation(id);
        userValidation(userId);

        filmDbStorage.addLike(id, userId);
        return filmDbStorage.getById(id);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        filmValidation(id);
        userValidation(userId);

        filmDbStorage.removeLike(id, userId);
        return filmDbStorage.getById(id);
    }

    @Override
    public List<Film> getPopular(Integer size) {
        return filmDbStorage.getPopular(size);
    }

    private void filmValidation(Integer id) {
        if (filmDbStorage.getById(id) == null) {
            throw new FilmValidationException("Фильм с id=" + id + " не найден в базе");
        }
    }

    private Film filmReleaseDateValidation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new InvalidReleaseDateFilmException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        return film;
    }

    private void userValidation(Integer id) {
        if (userService.getById(id) == null) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }
}