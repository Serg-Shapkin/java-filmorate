package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);
    User update(User user);
    List<User> getAll();
    User getById(Integer id);

    void addToFriends(Integer id, Integer friendId);             // добавить в друзья
    void removeFriend(Integer id, Integer friendId);             // удалить из друзей
    List<User> getFriendsById(Integer id);                       // получить друзей по id
    List<User> getCommonFriends(Integer id, Integer otherId);    // получить общих друзей
}