package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        long filmId = film.getId();
        films.put(filmId, film);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public HashMap<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public void addLikeFilm(long id, long userId) {
        Film film = films.get(id);
        film.getLikes().add(userId);
    }
    @Override
    public void removeLikeFilm(long id, long userId) {
        Film film = films.get(id);
        film.getLikes().remove(userId);
    }
}