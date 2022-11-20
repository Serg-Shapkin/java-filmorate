package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class InvalidFilmException extends RuntimeException {
    public InvalidFilmException(String message) {
        super(message);
    }
}
