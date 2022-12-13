package ru.yandex.practicum.filmorate.exception.film;

public class IncorrectFilmIdException extends RuntimeException { // некорректный id пользователя
    public IncorrectFilmIdException(String message) {
        super(message);
    }
}
