package ru.yandex.practicum.filmorate.exceptions.usersExceptions;

public class InvalidUserEmailException extends RuntimeException {
    public InvalidUserEmailException(String message) {
        super(message);
    }
}
