package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceDepartment {
    User addToFriends(long id, long friendId);
    User removeFriend(long id, long friendId);
    List<User> getFriendsById(long id);
    List<User> getCommonFriends(long id, long otherId);
}
