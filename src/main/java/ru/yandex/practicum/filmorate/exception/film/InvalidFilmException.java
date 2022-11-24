package ru.yandex.practicum.filmorate.exception.film;

public class InvalidFilmException extends RuntimeException {
    public InvalidFilmException(String message) {
        super(message);
    }
}
