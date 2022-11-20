package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class InvalidDurationFilmException extends RuntimeException {
    public InvalidDurationFilmException (String message) {
        super(message);
    }
}
