package ru.yandex.practicum.filmorate.exception.film;

public class InvalidCountLikesFilmException extends RuntimeException {
    public InvalidCountLikesFilmException(String message) {
        super(message);
    }
}
