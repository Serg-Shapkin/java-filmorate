package ru.yandex.practicum.filmorate.exception.film;

public class InvalidReleaseDateFilmException extends RuntimeException {
    public InvalidReleaseDateFilmException(String message) {
        super(message);
    }
}
