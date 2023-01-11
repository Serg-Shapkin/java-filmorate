package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);
    Film update(Film film);
    List<Film> getAll();
    Film getById(Integer id);

    void addLike(Integer id, Integer userId);
    void removeLike(Integer id, Integer userId);
    List<Film> getPopular(Integer size);
}