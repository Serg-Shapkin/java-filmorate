package ru.yandex.practicum.filmorate.exception.user;

public class UserDatabaseIsEmptyException extends RuntimeException {
    public UserDatabaseIsEmptyException(String message) {
        super(message);
    }
}
