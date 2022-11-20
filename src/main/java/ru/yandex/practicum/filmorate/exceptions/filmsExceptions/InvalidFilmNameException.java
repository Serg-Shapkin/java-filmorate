package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class InvalidFilmNameException extends RuntimeException {
    public InvalidFilmNameException (String message) {
        super(message);
    }
}
