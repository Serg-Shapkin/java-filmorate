package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FilmStorageTest extends InMemoryFilmStorage {
    FilmStorage filmStorage = new InMemoryFilmStorage();

    @DisplayName("Проверка добавления фильма")
    @Test
    void checkingAddFilm() {
        Film film1 = new Film(1, "TestFilm1", "Description_1", LocalDate.of(2022, 1,15),90);
        filmStorage.addFilm(film1);

        final long filmId = film1.getId();
        final Film savedFilm = filmStorage.getFilmById(filmId);

        assertNotNull(film1, "Фильм не найден");
        assertEquals(film1, savedFilm, "Фильмы не совпадают");

        final List<Film> films = new ArrayList<>(filmStorage.getAllFilms().values());

        assertNotNull(films, "Фильмы не возвращаются");
        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(film1, films.get(0), "Фильмы не совпадают");
    }

    @DisplayName("Проверка обновления данных о фильме")
    @Test
    void checkingFilmDataUpdates() {
        Film film1 = new Film(1, "TestFilm1", "Description_1", LocalDate.of(2022, 1,15),90);
        filmStorage.addFilm(film1);

        final long filmId = film1.getId();
        Film savedFilm = filmStorage.getFilmById(filmId);

        Film newFilm = new Film(1, "NewFilm", "Description_New", LocalDate.of(2022, 2,25),90);
        newFilm.setId(savedFilm.getId());

        savedFilm = newFilm;
        filmStorage.updateFilm(savedFilm);

        assertNotNull(savedFilm, "Фильм не найден");
        assertEquals(newFilm, savedFilm, "Задачи не совпадают");

        final List<Film> films = new ArrayList<>(filmStorage.getAllFilms().values());
        assertNotNull(films, "Фильмы не возвращаются");
        assertEquals(1, films.size(),"Неверное количество фильмов");
        assertEquals(newFilm, films.get(0),"Фильмы не совпадают");
    }

    @DisplayName("Проверка получения всех фильмов")
    @Test
    void checkingGetAllFilms() {
        Film film1 = new Film(1, "TestFilm1", "Description_1", LocalDate.of(2022, 1,15),90);
        Film film2 = new Film(2, "TestFilm2", "Description_2", LocalDate.of(2022, 2,25),90);

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        final List<Film> films = new ArrayList<>(filmStorage.getAllFilms().values());

        assertNotNull(films, "Фильмы не возвращаются");
        assertEquals(2, films.size(), "Неверное количество фильмов");
        assertEquals(film1, films.get(0), "Фильмы не совпадают");
    }

    @DisplayName("Проверка получения фильма по id")
    @Test
    void checkingGetFilmById() {
        Film film1 = new Film(1, "TestFilm1", "Description_1", LocalDate.of(2022, 1,15),90);
        filmStorage.addFilm(film1);

        final long filmId = film1.getId();
        final Film savedFilm = filmStorage.getFilmById(filmId);

        assertNotNull(savedFilm, "Фильм не найден");
        assertEquals(filmId, film1.getId(), "Id фильмов не совпадают");
    }
}
