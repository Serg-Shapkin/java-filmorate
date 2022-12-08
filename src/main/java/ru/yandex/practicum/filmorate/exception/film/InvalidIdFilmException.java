package ru.yandex.practicum.filmorate.exception.film;

public class InvalidIdFilmException extends RuntimeException {
    public InvalidIdFilmException(String message) {
        super(message);
    }
}
