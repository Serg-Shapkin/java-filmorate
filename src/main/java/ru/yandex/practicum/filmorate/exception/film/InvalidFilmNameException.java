package ru.yandex.practicum.filmorate.exception.film;

public class InvalidFilmNameException extends RuntimeException {
    public InvalidFilmNameException(String message) {
        super(message);
    }
}
