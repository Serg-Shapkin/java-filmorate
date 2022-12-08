package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserStorageTest extends InMemoryUserStorage {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @DisplayName("Проверка добавления пользователя")
    @Test
    void checkingAddUser() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        userStorage.addUser(user1);

        final long userId = user1.getId();
        final User savedUser = userStorage.getUserById(userId);

        assertNotNull(user1, "пользователь не найден");
        assertEquals(user1, savedUser, "Пользователи не совпадают");

        final List<User> users = userStorage.getAllUsers();

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

        final List<User> users = userStorage.getAllUsers();
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

        final List<User> users = userStorage.getAllUsers();

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

    @DisplayName("Проверка добавления в друзья")
    @Test
    void checkingAddFriendsUsers() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        user1.setFriends(new HashSet<>());
        User user2 = new User(2, "Oleg", "OlegIvanov", "olegivanov@ya.ru", LocalDate.of(1987, 5, 25));
        user2.setFriends(new HashSet<>());

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        final long userId = user1.getId();
        final long friendId = user2.getId();

        userStorage.addToFriends(userId, friendId);

        final User savedUser1 = userStorage.getUserById(userId);

        assertNotNull(savedUser1.getFriends(), "Друзья не найдены");
        assertEquals(1, savedUser1.getFriends().size(), "Неверное количество друзей");
    }

    @DisplayName("Проверка удаления пользователя из друзей")
    @Test
    void checkingRemoveFromFriends() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        user1.setFriends(new HashSet<>());
        User user2 = new User(2, "Oleg", "OlegIvanov", "olegivanov@ya.ru", LocalDate.of(1987, 5, 25));
        user2.setFriends(new HashSet<>());

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        final long userId = user1.getId();
        final long friendId = user2.getId();

        userStorage.addToFriends(userId, friendId);
        userStorage.removeFromFriends(userId, friendId);

        assertEquals(0, user1.getFriends().size(), "Неверное количество друзей");
    }

    @DisplayName("Проверка получения друзей пользователя")
    @Test
    void checkingGetFriendsById() {
        User user1 = new User(1, "Ivan", "IvanIvanov", "ivanivanov@ya.ru", LocalDate.of(2000, 10, 5));
        user1.setFriends(new HashSet<>());
        User user2 = new User(2, "Oleg", "OlegIvanov", "olegivanov@ya.ru", LocalDate.of(1987, 5, 25));
        User user3 = new User(3, "Olga", "OlgaOlga", "olga@ya.ru", LocalDate.of(1999, 7, 12));


        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        final long userId = user1.getId();
        final long friendIdOne = user2.getId();
        final long friendIdTwo = user3.getId();

        userStorage.addToFriends(userId, friendIdOne);
        userStorage.addToFriends(userId, friendIdTwo);

        User savedUser = userStorage.getFriendsById(userId);
        Set<Long> friendsId = savedUser.getFriends();
        final List<User> friendsUser = new ArrayList<>();

        for (Long friends : friendsId) {
            User userFriend = userStorage.getFriendsById(friends);
            friendsUser.add(userFriend);
        }

        assertNotNull(friendsUser, "Друзья не найдены");
        assertEquals(2, friendsUser.size(), "Неверное количество друзей");
    }
}