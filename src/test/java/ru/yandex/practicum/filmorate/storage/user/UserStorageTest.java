package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserStorageTest extends InMemoryUserStorage {
    UserStorage userStorage = new InMemoryUserStorage();

    @DisplayName("Проверка добавления пользователя")
    @Test
    void checkingAddUser() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        userStorage.addUser(user1);

        final long userId = user1.getId();
        final User savedUser = userStorage.getUserById(userId);

        assertNotNull(user1, "Пользователь не найден");
        assertEquals(user1, savedUser, "Пользователи не совпадают");

        final List<User> users = new ArrayList<>(userStorage.getAllUsers().values());

        assertNotNull(users, "Пользователи не возвращаются");
        assertEquals(1, users.size(), "Неверное количество фильмов");
        assertEquals(user1, users.get(0), "Фильмы не совпадают");
    }

    @DisplayName("Проверка обновления данных о пользователе")
    @Test
    void checkingUserDataUpdates() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        userStorage.addUser(user1);

        final long userId = user1.getId();
        User savedUser = userStorage.getUserById(userId);

        User newUser = new User(1, "Oleg", "OlegIvanov", "olegivanov@ya.ru", LocalDate.of(1987, 5, 25));
        newUser.setId(savedUser.getId());

        savedUser = newUser;
        userStorage.updateUser(savedUser);


        assertNotNull(savedUser, "Пользователь не найден");
        assertEquals(newUser, savedUser, "Пользователи не совпадают");

        final List<User> users = new ArrayList<>(userStorage.getAllUsers().values());
        assertNotNull(users, "Пользователи не возвращаются");
        assertEquals(1, users.size(),"Неверное количество пользователей");
        assertEquals(newUser, users.get(0),"Пользователи не совпадают");
    }

    @DisplayName("Проверка получения всех пользователей")
    @Test
    void checkingGetAllUsers() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        User user2 = new User(2, "Oleg", "OlegIvanov", "olegivanov@ya.ru", LocalDate.of(1987, 5, 25));

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        final List<User> users = new ArrayList<>(userStorage.getAllUsers().values());

        assertNotNull(users, "Пользователи не возвращаются");
        assertEquals(2, users.size(),"Неверное количество пользователей");
        assertEquals(user1, users.get(0),"Пользователи не совпадают");
    }

    @DisplayName("Проверка получения пользователя по id")
    @Test
    void checkingGetUserById() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        userStorage.addUser(user1);

        final long userId = user1.getId();
        final User savedUser = userStorage.getUserById(userId);

        assertNotNull(savedUser, "Пользователь не найден");
        assertEquals(userId, user1.getId(), "Id пользователей не совпадают");
    }
}