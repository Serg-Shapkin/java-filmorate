package ru.yandex.practicum.filmorate.exception.user;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException (String message) {
        super(message);
    }
}