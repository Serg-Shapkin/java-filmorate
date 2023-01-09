package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();
    Film getFilmById(Integer id);

    void addLikeFilm(Integer id, Integer userId);
    void removeLikeFilm(Integer id, Integer userId);
    List<Film> getPopularFilms(Integer size);
}