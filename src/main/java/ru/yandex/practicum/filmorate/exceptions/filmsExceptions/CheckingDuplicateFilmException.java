package ru.yandex.practicum.filmorate.exceptions.filmsExceptions;

public class CheckingDuplicateFilmException extends RuntimeException{
    public CheckingDuplicateFilmException(String message) {
        super(message);
    }
}
