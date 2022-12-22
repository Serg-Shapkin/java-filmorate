package ru.yandex.practicum.filmorate.exception.user;

public class UserDatabaseIsEmptyException extends RuntimeException { // пустая база пользователей
    public UserDatabaseIsEmptyException(String message) {
        super(message);
    }
}
