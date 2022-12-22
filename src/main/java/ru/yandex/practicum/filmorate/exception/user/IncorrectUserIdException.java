package ru.yandex.practicum.filmorate.exception.user;

public class IncorrectUserIdException  extends RuntimeException { // некорректный id пользователя
    public IncorrectUserIdException(String message) {
        super(message);
    }
}
