package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceDepartment {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    public UserService() {
        this.userStorage = new InMemoryUserStorage();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.getAllUsers().values());
    }

    public User getUserById(long id) {
        userValidation(id);
        return userStorage.getUserById(id);
    }

    @Override
    public User addToFriends(long id, long friendId) { // id - кому? friendId - кого?
        userValidation(id);
        userValidation(friendId);

        User user = userStorage.getAllUsers().get(id);
        User friendUser = userStorage.getAllUsers().get(friendId);

        user.getFriends().add(friendId); // прямое добавление в друзья
        log.info("Пользователь {} добавил в друзья пользователя {}", user.getName(), friendUser.getName());
        friendUser.getFriends().add(id); // обратное добавление в друзья
        log.info("Пользователь {} добавил в друзья пользователя {}", friendUser.getName(), user.getName());

        return user;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        userValidation(id);
        userValidation(friendId);

        User user = userStorage.getAllUsers().get(id);
        User friendUser = userStorage.getAllUsers().get(friendId);

        user.getFriends().remove(friendId); // прямое удаление из друзей
        log.info("Пользователь {} удалил из друзей пользователя {}", user.getName(), friendUser.getName());
        friendUser.getFriends().remove(id); // обратное удаление из друзей
        log.info("Пользователь {} удалил из друзей пользователя {}", friendUser.getName(), user.getName());

        return user;
    }

    @Override
    public List<User> getFriendsById(long id) {
        userValidation(id);

        User user = getUserById(id);                // получили пользователя
        List<User> friendsUser = new ArrayList<>(); // создаем список для хранения друзей

        Set<Long> friendsId = user.getFriends(); // получили id всех друзей

        for (Long friend : friendsId) {
            User userFriend = getUserById(friend);
            friendsUser.add(userFriend);
            }

        log.info("Запрошен список друзей пользователя {}", user.getName());

        return friendsUser;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) { // общие друзья
        userValidation(id);
        userValidation(otherId);

        List<User> userOne = getFriendsById(id);
        List<User> userTwo = getFriendsById(otherId);

        List<User> common = userOne.stream()
                .filter(userTwo::contains)
                .collect(Collectors.toList());
        log.info("Запрошен общий список друзей друзей");

        return common;
    }

    private void userValidation(long id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }
}