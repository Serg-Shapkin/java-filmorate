package ru.yandex.practicum.filmorate.exceptions.usersExceptions;

public class InvalidUserBirthdayException extends RuntimeException {
    public InvalidUserBirthdayException(String message) {
        super(message);
    }
}
