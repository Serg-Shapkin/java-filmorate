package ru.yandex.practicum.filmorate.exception.user;

public class UserValidationException extends RuntimeException { // пользователь по id отсутствует в базе
    public UserValidationException(String message) {
        super(message);
    }
}
