package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    void addUser(User user);                         // добавить пользователя
    void updateUser(User user);                      // обновить пользователя
    List<User> getAllUsers();                        // получить список всех пользователей
    HashMap<Long, User> getUsers();                  // просто возвращает HashMap
    User getUserById(long id);                       // получить пользователя по id
    void addToFriends(long id, long friendId);       // добавить пользователя в друзья
    void removeFromFriends(long id, long friendId);  // удалить пользователя из друзей
    User getFriendsById(long id);                    // получить друзей пользователя по id
}