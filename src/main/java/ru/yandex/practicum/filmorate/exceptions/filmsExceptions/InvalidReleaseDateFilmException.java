package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class InvalidReleaseDateFilmException extends RuntimeException {
    public InvalidReleaseDateFilmException(String message) {
        super(message);
    }
}
