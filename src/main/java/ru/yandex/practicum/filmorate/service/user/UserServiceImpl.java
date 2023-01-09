package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDbStorage userDbStorage; // изменил

    public UserServiceImpl(UserDbStorage userStorage) {
        this.userDbStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidation(user.getId());
        return userDbStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userDbStorage.getAllUsers());
    }

    @Override
    public User getUserById(Integer id) {
        userValidation(id);
        return userDbStorage.getUserById(id);
    }

    @Override
    public User addToFriends(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        userDbStorage.addToFriends(id, friendId);
        return userDbStorage.getUserById(id);
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        userDbStorage.removeFriend(id, friendId);
        return userDbStorage.getUserById(id);
    }

    @Override
    public List<User> getFriendsById(Integer id) {
        userValidation(id);
        return userDbStorage.getFriendsById(id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) { // общие друзья
        userValidation(id);
        userValidation(otherId);

        return userDbStorage.getCommonFriends(id, otherId);
    }

    private void userValidation(Integer id) {
        if (userDbStorage.getUserById(id) == null) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }
}
