package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);                         // добавить пользователя
    User updateUser(User user);                      // обновить пользователя
    Map<Long, User> getAllUsers();                   // получить список всех пользователей
    User getUserById(long id);                       // получить пользователя по id
}