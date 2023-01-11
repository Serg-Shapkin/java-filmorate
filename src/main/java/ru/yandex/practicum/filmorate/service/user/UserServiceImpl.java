package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDbStorage userDbStorage; // изменил

    @Override
    public User add(User user) {
        userNameValidation(user); // проверка имени пользователя
        return userDbStorage.add(user);
    }

    @Override
    public User update(User user) {
        userValidation(user.getId());
        userNameValidation(user); // проверка имени пользователя
        return userDbStorage.update(user);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userDbStorage.getAll());
    }

    @Override
    public User getById(Integer id) {
        userValidation(id);
        return userDbStorage.getById(id);
    }

    @Override
    public User addToFriends(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        userDbStorage.addToFriends(id, friendId);
        return userDbStorage.getById(id);
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        userDbStorage.removeFriend(id, friendId);
        return userDbStorage.getById(id);
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
        if (userDbStorage.getById(id) == null) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }

    private User userNameValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin()); // именем будет логин
        }
        return user;
    }
}
