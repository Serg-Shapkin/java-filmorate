package ru.yandex.practicum.filmorate.exception.user;

public class InvalidUserBirthdayException extends RuntimeException {
    public InvalidUserBirthdayException(String message) {
        super(message);
    }
}
