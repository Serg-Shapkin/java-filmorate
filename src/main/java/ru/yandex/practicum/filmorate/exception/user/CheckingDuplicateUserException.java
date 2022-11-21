package ru.yandex.practicum.filmorate.exception.user;

public class CheckingDuplicateUserException extends RuntimeException {
    public CheckingDuplicateUserException(String message) {
        super(message);
    }
}
