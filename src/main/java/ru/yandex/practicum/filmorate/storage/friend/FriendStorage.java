package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addToFriends(Integer id, Integer friendId);
    void removeFriend(Integer id, Integer friendId);
}
