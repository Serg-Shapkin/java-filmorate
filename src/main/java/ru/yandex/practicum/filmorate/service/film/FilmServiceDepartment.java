package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceDepartment {

    Film addLikeFilm(long id, long userId);

    Film removeLikeFilm(long id, long userId);

    List<Film> getPopularFilms(long size);
}