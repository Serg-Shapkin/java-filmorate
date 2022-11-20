package ru.yandex.practicum.filmorate.exceptions.usersExceptions;

public class InvalidUserLoginException extends RuntimeException {
    public InvalidUserLoginException(String message) {
        super(message);
    }
}
