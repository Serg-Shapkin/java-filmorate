package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.exception.genre.IncorrectGenreIdException;
import ru.yandex.practicum.filmorate.exception.mpa.IncorrectMpaIdException;
import ru.yandex.practicum.filmorate.exception.user.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exception.user.UserDatabaseIsEmptyException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - фильм добавлен ранее
    public ErrorResponse handleFilmValidationException(final FilmValidationException e) {
        log.error("Фильм не найден в базе");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - фильм добавлен ранее/отсутствует в базе
    public ErrorResponse handleIncorrectFilmIdException(final IncorrectFilmIdException e) {
        log.error("Указан некорректный id фильма");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пустое название фильма
    public ErrorResponse handleInvalidFilmNameException(final InvalidFilmNameException e) {
        log.error("Название фильма не может быть пустым");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - ошибка дата релиза фильма
    public ErrorResponse handleInvalidReleaseDateFilmException(final InvalidReleaseDateFilmException e) {
        log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 400 - пользователь добавлен ранее / указан некорректный id
    public ErrorResponse handleIncorrectUserIdException(final IncorrectUserIdException e) {
        log.error("Указан некорректный id пользователя или пользователь был добавлен ранее");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - нет пользователей в базе
    public ErrorResponse handleUserDatabaseIsEmptyException(final UserDatabaseIsEmptyException e) {
        log.error("В базе не сохранено ни одного пользователя");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - пользователь не найден в базе
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        log.error("Пользователь не найден в базе");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    // обработчик для исключения, которое выбрасывается при валидации данных с помощью аннотаций MethodArgumentNotValidException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("Некорректные данные от пользователя");
        return  new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - рейтинг не найден в базе
    public ErrorResponse handleIncorrectMpaIdException(final IncorrectMpaIdException e) {
        log.error("Указан некорректный id рейтинга");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - жанр не найден в базе
    public ErrorResponse handleIncorrectGenreIdException(final IncorrectGenreIdException e) {
        log.error("Указан некорректный id жанра");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIncorrectParameterException(IncorrectParameterException e) {
        log.error("Передан некорректный параметр: {}", e.getParameter());
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // код ответа 500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка");
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }


}
