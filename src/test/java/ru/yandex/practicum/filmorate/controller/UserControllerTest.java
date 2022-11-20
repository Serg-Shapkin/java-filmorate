package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends UserController {
    private final UserController userController = new UserController();

    @DisplayName("Проверка добавления пользователя с одинаковым логином")
    @Test
    void CheckingForAddingUserWithTheSameName() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "test@yandex.ru", "testUser", "Ivan Ivanov",
                                LocalDate.of(1990, 10, 10));
                        userController.addUser(user);
                        User user1 = new User(1, "test1@yandex.ru", "testUser", "Vasiliy Ivanov",
                                LocalDate.of(1968, 05, 12));
                        userController.addUser(user1);
                    }
                });
        assertEquals("Пользователь с логином \"testUser\" был создан ранее.", exception.getMessage());
    }

    @DisplayName("Проверка электронной почты на пустоту")
    @Test
    void CheckingForAnEmptyEmailUser() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "", "testUser", "Ivan Ivanov",
                                LocalDate.of(1990, 10, 10));
                        userController.addUser(user);
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ \"@\".", exception.getMessage());
    }

    @DisplayName("Проверка электронной почты на символ @")
    @Test
    void CheckingEmailFotACharacter0040() { // U+0040 = @
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "test!yandex.ru", "testUser", "Ivan Ivanov",
                                LocalDate.of(1990, 10, 10));
                        userController.addUser(user);
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ \"@\".", exception.getMessage());
    }


    @DisplayName("Проверка логина пользователя на пустоту")
    @Test
    void CheckingTheUserNameForEmptiness() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "test@yandex.ru", "", "Ivan Ivanov",
                                LocalDate.of(1990, 10, 10));
                        userController.addUser(user);
                    }
                });
        assertEquals("Логин пользователя не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @DisplayName("Проверка логина пользователя на пробелы")
    @Test
    void CheckingTheUserNameForSpaces() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "test@yandex.ru", " ", "Ivan Ivanov",
                                LocalDate.of(1990, 10, 10));
                        userController.addUser(user);
                    }
                });
        assertEquals("Логин пользователя не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @DisplayName("Проверка даты рождения пользователя")
    @Test
    void CheckingTheUsersDateOfBirthday() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        User user = new User(1, "test@yandex.ru", "testUser", "Ivan Ivanov",
                                LocalDate.of(2025, 10, 10));
                        userController.addUser(user);
                    }
                });
        assertEquals("Дата рождения пользователя не может быть в будущем.", exception.getMessage());
    }
}
