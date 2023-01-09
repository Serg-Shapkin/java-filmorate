package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.user.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exception.user.UserDatabaseIsEmptyException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private final static Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    public final HashMap<Integer, User> users = new HashMap<>();

    private int userId = 1;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin()); // именем будет логин
        } else if (users.containsKey(user.getId())) {
            log.info("Пользователь {} был добавлен ранее", user.getName());
            throw new IncorrectUserIdException(String.format("Пользователь %s был добавлен ранее", user.getName()));
        }
        user.setId(userId);
        users.put(user.getId(), user);
        userId++;
        log.info("Пользователь {} успешно добавлен", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin()); // именем будет логин
        } else if (!users.containsKey(user.getId())) {
            log.error("Пользователь {} не найден в базе", user.getName());
            throw new UserValidationException(String.format("Пользователь %s не найден в базе", user.getName()));
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя {} успешно обновлены", user.getName());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        if (users.isEmpty()) {
            log.error("В базе не сохранено ни одного пользователя");
            throw new UserDatabaseIsEmptyException("В базе не сохранено ни одного пользователя");
        }
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) {   // получить пользователя по id
        if (!users.containsKey(id)) {
            log.error("Указан некорректный id пользователя");
            throw new IncorrectUserIdException("Указан некорректный id пользователя"); // 404
        }
        log.info("Запрошен пользователь c id={}", id);
        return users.get(id);
    }

    @Override
    public void addToFriends(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        User user = users.get(id);
        User friendUser = users.get(friendId);

        user.getFriends().add(friendId); // прямое добавление в друзья
        log.info("Пользователь {} добавил в друзья пользователя {}", user.getName(), friendUser.getName());
        friendUser.getFriends().add(id); // обратное добавление в друзья
        log.info("Пользователь {} добавил в друзья пользователя {}", friendUser.getName(), user.getName());
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {
        userValidation(id);
        userValidation(friendId);

        User user = users.get(id);
        User friendUser = users.get(friendId);

        user.getFriends().remove(friendId); // прямое удаление из друзей
        log.info("Пользователь {} удалил из друзей пользователя {}", user.getName(), friendUser.getName());
        friendUser.getFriends().remove(id); // обратное удаление из друзей
        log.info("Пользователь {} удалил из друзей пользователя {}", friendUser.getName(), user.getName());
    }

    @Override
    public List<User> getFriendsById(Integer id) {
        userValidation(id);

        User user = getUserById(id);                // получили пользователя
        List<User> friendsUser = new ArrayList<>(); // создаем список для хранения друзей

        Set<Integer> friendsId = user.getFriends(); // получили id всех друзей

        for (Integer friend : friendsId) {
            User userFriend = getUserById(friend);
            friendsUser.add(userFriend);
        }

        log.info("Запрошен список друзей пользователя {}", user.getName());

        return friendsUser;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
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

    private void userValidation(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserValidationException(String.format("Пользователь с id=%s не найден в базе", id));
        }
    }
}