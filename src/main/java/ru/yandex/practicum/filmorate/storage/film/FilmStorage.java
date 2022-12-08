package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);
    void updateFilm(Film film);
    List<Film> getAllFilms();
    HashMap<Long, Film> getFilms();
    Film getFilmById(long id);                       // получить фильм по id
    void addLikeFilm(long id, long userId);          // пользователь ставит лайк фильму
    void removeLikeFilm(long id, long userId);       // пользователь удаляет лайк у фильма
}