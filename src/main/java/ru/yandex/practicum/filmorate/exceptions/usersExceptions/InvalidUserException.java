package ru.yandex.practicum.filmorate.exceptions.usersExceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException (String message) {
        super(message);
    }
}
