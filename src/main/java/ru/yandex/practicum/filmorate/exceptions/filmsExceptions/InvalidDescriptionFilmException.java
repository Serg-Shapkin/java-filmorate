package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class InvalidDescriptionFilmException extends RuntimeException {
    public InvalidDescriptionFilmException(String message) {
        super(message);
    }
}
