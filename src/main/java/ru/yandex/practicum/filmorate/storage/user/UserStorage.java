package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);                                    // добавить пользователя
    User updateUser(User user);                                 // обновить пользователя
    List<User> getAllUsers();                                   // получить список всех пользователей
    User getUserById(Integer id);                                // получить пользователя по id

    void addToFriends(Integer id, Integer friendId);             // добавить в друзья
    void removeFriend(Integer id, Integer friendId);             // удалить из друзей
    List<User> getFriendsById(Integer id);                       // получить друзей по id
    List<User> getCommonFriends(Integer id, Integer otherId);    // получить общих друзей
}