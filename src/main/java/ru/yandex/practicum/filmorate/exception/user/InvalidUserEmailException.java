package ru.yandex.practicum.filmorate.exception.user;

public class InvalidUserEmailException extends RuntimeException {
    public InvalidUserEmailException(String message) {
        super(message);
    }
}
