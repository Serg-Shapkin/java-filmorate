package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();
    Film getFilmById(Integer id);

    Film addLikeFilm(Integer id, Integer userId);
    Film removeLikeFilm(Integer id, Integer userId);
    List<Film> getPopularFilms(Integer size);
}