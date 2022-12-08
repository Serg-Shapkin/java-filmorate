package ru.yandex.practicum.filmorate.exception.film;

public class DuplicateFilmException extends RuntimeException {
    public DuplicateFilmException(String message) {
        super(message);
    }
}
