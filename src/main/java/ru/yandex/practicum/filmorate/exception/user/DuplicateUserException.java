package ru.yandex.practicum.filmorate.exception.user;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
