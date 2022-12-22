package ru.yandex.practicum.filmorate.exception.film;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String message) { // Фильм с id не найден в базе
        super(message);
    }
}
