package ru.yandex.practicum.filmorate.exception.film;

public class InvalidDescriptionFilmException extends RuntimeException {
    public InvalidDescriptionFilmException(String message) {
        super(message);
    }
}
