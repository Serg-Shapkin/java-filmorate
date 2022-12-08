package ru.yandex.practicum.filmorate.exception.film;

public class InvalidRemoveLikeFilmException extends RuntimeException {
    public InvalidRemoveLikeFilmException(String message) {
        super(message);
    }
}
