package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest extends FilmController{
    private final FilmController filmController = new FilmController();

    @DisplayName("Проверка добавления фильма с одинаковым названием")
    @Test
    void CheckingForAddingFilmWithTheSameName() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film(2, "TestFilm", "Description",
                                LocalDate.of(2022, 01,15),90);
                        filmController.addFilm(film);
                        Film film2 = new Film(2, "TestFilm", "Description",
                                LocalDate.of(2022, 6,25),90);
                        filmController.addFilm(film2);
                    }
                });
        assertEquals("Фильм с названием \"TestFilm\" был добавлен ранее.", exception.getMessage());
    }

    @DisplayName("Проверка названия фильма на пустоту")
    @Test
    void CheckingForAnEmptyFilmName() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film(2, "", "Description",
                                LocalDate.of(2022, 01,15),90);
                        filmController.addFilm(film);
                    }
                });
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @DisplayName("Проверка на длину описания фильма")
    @Test
    void CheckingForTheLengthOfTheMovieDescription() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film(2, "TestFilm",
                                "DescriptionDescriptionDescriptionDescriptionDescription" + // 55
                                        "DescriptionDescriptionDescriptionDescriptionDescription" +   // 110
                                        "DescriptionDescriptionDescriptionDescriptionDescription" +   // 165
                                        "DescriptionDescriptionDescriptionDescriptionDescription",    // 220
                                LocalDate.of(2022, 01,15),90);
                        filmController.addFilm(film);
                    }
                });
        assertEquals("Описание фильма не может быть более 200 символов.", exception.getMessage());
    }

    @DisplayName("Проверка даты релиза фильма")
    @Test
    void CheckingTheReleaseDateOfTheFilm() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film(2, "TestFilm", "Description",
                                LocalDate.of(1895, 12,25),90);
                        filmController.addFilm(film);
                    }
                });
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @DisplayName("Проверка продолжительности фильма")
    @Test
    void CheckingTheDurationOfTheFilm() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Film film = new Film(2, "TestFilm", "Description",
                                LocalDate.of(2022, 01,15),-10);
                        filmController.addFilm(film);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }
}
