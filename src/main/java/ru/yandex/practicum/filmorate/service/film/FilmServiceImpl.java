package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceImpl {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(long id);

    Film addLikeFilm(long id, long userId);

    Film removeLikeFilm(long id, long userId);

    List<Film> getPopularFilms(long size);
}