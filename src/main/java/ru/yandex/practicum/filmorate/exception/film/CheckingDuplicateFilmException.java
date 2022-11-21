package ru.yandex.practicum.filmorate.exception.film;

public class CheckingDuplicateFilmException extends RuntimeException{
    public CheckingDuplicateFilmException(String message) {
        super(message);
    }
}
