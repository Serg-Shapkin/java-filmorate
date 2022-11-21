package ru.yandex.practicum.filmorate.exception.film;

public class InvalidDurationFilmException extends RuntimeException {
    public InvalidDurationFilmException (String message) {
        super(message);
    }
}
