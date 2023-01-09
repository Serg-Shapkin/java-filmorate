package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User updateUser(User user);
    List<User> getAllUsers();
    User getUserById(Integer id);

    User addToFriends(Integer id, Integer friendId);
    User removeFriend(Integer id, Integer friendId);
    List<User> getFriendsById(Integer id);
    List<User> getCommonFriends(Integer id, Integer otherId);
}
