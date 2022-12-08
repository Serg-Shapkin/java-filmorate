package ru.yandex.practicum.filmorate.exception.user;

public class InvalidIdUserException extends RuntimeException {
    public InvalidIdUserException(String message) {
        super(message);
    }
}
