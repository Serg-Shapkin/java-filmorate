package ru.yandex.practicum.filmorate.exceptions.usersExceptions;

public class CheckingDuplicateUserException extends RuntimeException {
    public CheckingDuplicateUserException(String message) {
        super(message);
    }
}
