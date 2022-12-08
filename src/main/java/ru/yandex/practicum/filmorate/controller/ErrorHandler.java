package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - фильм добавлен ранее
    public ErrorResponse handleDuplicateFilmException(final DuplicateFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пустое название фильма
    public ErrorResponse handlerInvalidFilmNameException(final InvalidFilmNameException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - длина описания фильма
    public ErrorResponse handlerInvalidDescriptionFilmException(final InvalidDescriptionFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - дата релиза
    public ErrorResponse handlerInvalidReleaseDateFilmException(final InvalidReleaseDateFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - продолжительность фильма
    public ErrorResponse handlerInvalidDurationFilmException(final InvalidDurationFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - фильм отсутствует в базе (обновление)
    public ErrorResponse handlerInvalidFilmException(final InvalidFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пустое хранилище фильмов (getAllFilms, getPopularFilms)
    public ErrorResponse handlerFilmDatabaseIsEmptyException(final FilmDatabaseIsEmptyException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - фильм по id не найден (getFilmById, addLikeFilm, removeLikeFilm)
    public ErrorResponse handlerInvalidIdFilmException(final InvalidIdFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    //***
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - пользователь по id не найден (addLikeFilm, removeLikeFilm, getUserById)
    public ErrorResponse handlerInvalidIdUserException(final InvalidIdUserException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - у фильма еще нет лайков
    public ErrorResponse handlerInvalidCountLikesFilmException(final InvalidCountLikesFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пользователь еще не поставил лайк этому фильму
    public ErrorResponse handlerInvalidRemoveLikeFilmException(final InvalidRemoveLikeFilmException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пользователь был созлан ранее
    public ErrorResponse handlerDuplicateUserException(final DuplicateUserException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - проверка электронной почты пользователя
    public ErrorResponse handlerInvalidUserEmailException(final InvalidUserEmailException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - проверка логина пользователя
    public ErrorResponse handlerInvalidUserLoginException(final InvalidUserLoginException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - проверка даты рождения пользователя
    public ErrorResponse handlerInvalidUserBirthdayException(final InvalidUserBirthdayException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // код 404 - пользователь отсутствует в базе (обновление)
    public ErrorResponse handlerInvalidUserException(final InvalidUserException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - пустое хранилище пользователей (getAllUsers)
    public ErrorResponse handlerUserDatabaseIsEmptyException(final UserDatabaseIsEmptyException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - ошибка добавления в друзья самого себя
    public ErrorResponse handlerWhenAddingUsersException(final WhenAddingUsersException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // код 400 - ошибка добавления в друзья самого себя
    public ErrorResponse handlerRemoveFromFriendsException(final RemoveFromFriendsException e) {
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
