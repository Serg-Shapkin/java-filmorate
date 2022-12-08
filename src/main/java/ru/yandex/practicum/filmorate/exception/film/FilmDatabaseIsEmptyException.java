package ru.yandex.practicum.filmorate.exception.film;

public class FilmDatabaseIsEmptyException extends RuntimeException {
    public FilmDatabaseIsEmptyException(String message) {
        super(message);
    }
}
