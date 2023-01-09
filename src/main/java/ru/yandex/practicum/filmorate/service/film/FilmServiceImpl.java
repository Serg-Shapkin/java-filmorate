package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserServiceImpl userServiceImpl;

    public FilmServiceImpl(FilmDbStorage filmDbStorage, UserServiceImpl userServiceImpl) {
        this.filmDbStorage = filmDbStorage;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Film addFilm(Film film) {
        return filmDbStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidation(film.getId());
        return filmDbStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmDbStorage.getAllFilms());
    }

    @Override
    public Film getFilmById(Integer id) {
        filmValidation(id);
        return filmDbStorage.getFilmById(id);
    }

    @Override
    public Film addLikeFilm(Integer id, Integer userId) {
        filmValidation(id);
        userValidation(userId);

        filmDbStorage.addLikeFilm(id, userId);
        return filmDbStorage.getFilmById(id);
    }

    @Override
    public Film removeLikeFilm(Integer id, Integer userId) {
        filmValidation(id);
        userValidation(userId);

        filmDbStorage.removeLikeFilm(id, userId);
        return filmDbStorage.getFilmById(id);
    }

    @Override
    public List<Film> getPopularFilms(Integer size) {
        return filmDbStorage.getPopularFilms(size);
    }

    private void filmValidation(Integer id) {
        if (filmDbStorage.getFilmById(id) == null) {
            throw new FilmValidationException("Фильм с id=" + id + " не найден в базе");
        }
    }

    private void userValidation(Integer id) {
        if (userServiceImpl.getUserById(id) == null) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }
}