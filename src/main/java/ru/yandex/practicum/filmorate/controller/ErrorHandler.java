package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.film.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.exception.film.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exception.film.InvalidReleaseDateFilmException;
import ru.yandex.practicum.filmorate.exception.genre.IncorrectGenreIdException;
import ru.yandex.practicum.filmorate.exception.mpa.IncorrectMpaIdException;
import ru.yandex.practicum.filmorate.exception.user.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exception.user.UserDatabaseIsEmptyException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - фильм добавлен ранее
    public ErrorResponse handleFilmValidationException(final FilmValidationException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - фильм добавлен ранее/отсутствует в базе
    public ErrorResponse handleIncorrectFilmIdException(final IncorrectFilmIdException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пустое название фильма
    public ErrorResponse handleInvalidFilmNameException(final InvalidFilmNameException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - ошибка дата релиза фильма
    public ErrorResponse handleInvalidReleaseDateFilmException(final InvalidReleaseDateFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 400 - пользователь добавлен ранее / указан некорректный id
    public ErrorResponse handleIncorrectUserIdException(final IncorrectUserIdException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - нет пользователей в базе
    public ErrorResponse handleUserDatabaseIsEmptyException(final UserDatabaseIsEmptyException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - пользователь не найден в базе
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - рейтинг не найден в базе
    public ErrorResponse handleIncorrectMpaIdException(final IncorrectMpaIdException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - жанр не найден в базе
    public ErrorResponse handleIncorrectGenreIdException(final IncorrectGenreIdException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // код ответа 500
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
