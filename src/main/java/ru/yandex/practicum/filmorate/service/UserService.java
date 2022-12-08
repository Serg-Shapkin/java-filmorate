package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);
    private long userId = 1;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (userStorage.getUsers().containsValue(user)) {
            log.error("Пользователь с таким логином был создан ранее.");
            throw new DuplicateUserException("Пользователь с таким логином был создан ранее.");

        } else if (user.getEmail() == null || (!user.getEmail().contains("@"))) { // пусто или не содержит @
            log.error("Электронная почта не может быть пустой и должна содержать символ @.");
            throw new InvalidUserEmailException("Электронная почта не может быть пустой и должна содержать символ @.");

        } else if (user.getLogin() == null
                || user.getLogin().isBlank()        // isBlank - пустой ли?
                || user.getLogin().contains(" ")) { // или содержит пробелы
            log.error("Логин пользователя не может быть пустым или содержать пробелы.");
            throw new InvalidUserLoginException("Логин пользователя не может быть пустым или содержать пробелы.");

        } else if (user.getBirthday().isAfter(LocalDate.now())) { // isAfter - позже
            log.error("Дата рождения пользователя не может быть в будущем. {}", user);
            throw new InvalidUserBirthdayException("Дата рождения пользователя не может быть в будущем.");

        } else if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin()); // именем будет логин
            log.info("Пользователь {} успешно добавлен.", user.getName());
            return getUser(user);
        } else {
            log.info("Пользователь {} успешно добавлен.", user.getName());
            return getUser(user);
        }
    }

    private User getUser(User user) {
        user.setId(userId);
        userStorage.addUser(user);
        userId++;
        return user;
    }

    public User updateUser(User user) {
        if (!userStorage.getUsers().containsKey(user.getId())) {
            log.error("Невозможно обновить данные о пользователе. Пользователь отсутствует в базе.");
            throw new InvalidUserException("Невозможно обновить данные о пользователе. Пользователь отсутствует в базе.");
        } else {
            userStorage.updateUser(user);
            log.info("Данные пользователя {} успешно обновлены.", user.getName());
            return user;
        }
    }

    public List<User> getAllUsers() {
        if (userStorage.getAllUsers() == null || userStorage.getAllUsers().isEmpty()) {
            log.error("В базе не сохранено ни одного пользователя.");
            throw new UserDatabaseIsEmptyException("В базе не сохранено ни одного пользователя.");
        }

        log.info("Запрошен список всех пользователей.");
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        if (userStorage.getUserById(id) == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректный id пользователя.");
        }
        log.info("Запрошен пользователь c id {}.", id);
        return userStorage.getUserById(id);
    }

    public User addToFriends(long id, long friendId) { // id - кому? friendId - кого?
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректный id пользователя.");
        }

        User friendUser = userStorage.getUserById(friendId);
        if (friendUser == null) {
            log.error("Указан некорректныйй id друга пользователя.");
            throw new InvalidIdUserException("Указан некорректныйй id друга пользователя.");
        }

        addFriend(id, friendId, user, friendUser);
        addFriend(friendId, id, friendUser, user);

        return user;
    }

    private void addFriend(long id, long friendId, User user, User friendUser) {
        if (friendUser.getFriends() == null) {
            friendUser.setFriends(new HashSet<>());
        }

        if (friendUser.getId() == id) {
            log.error("Добавить самого себя в друзья невозможно.");
            throw new WhenAddingUsersException("Добавить самого себя в друзья невозможно.");
        } else {
            userStorage.addToFriends(friendId, id);
            log.info("Пользователь {} добавил в друзья пользователя {}.", friendUser.getName(), user.getName());
        }
    }

    public User removeFromFriends(long id, long friendId) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректный id пользователя.");
        }

        User friendUser = userStorage.getUserById(friendId);
        if (friendUser == null) {
            log.error("Указан некорректныйй id друга пользователя.");
            throw new InvalidIdUserException("Указан некорректныйй id друга пользователя.");
        }

        Set<Long> friendsId = user.getFriends(); // получили id всех друзей

        if (user.getFriends() == null) {
            log.error("У пользователя {} еще нет друзей", user.getName());
            throw new RemoveFromFriendsException("Удаление невозможно. У пользователя еще нет друзей.");

        } else if (!friendsId.contains(friendId)) {
            log.error("Удаление невозможно. Пользователь {} не является другом пользователя {}", user.getName(), friendUser.getName());
            log.error("Удаление невозможно. Пользователь {} не является другом пользователя {}", friendUser.getName(), user.getName());
            throw new RemoveFromFriendsException("Удаление невозможно. Пользователи не являются друзьями.");
        } else {
            userStorage.removeFromFriends(id, friendId);
            log.info("Пользователь {} удалил из друзей пользователя {}.", user.getName(), friendUser.getName());

            userStorage.removeFromFriends(friendId, id); // обратное удаление
            log.info("Пользователь {} удалил из друзей пользователя {}.", friendUser.getName(), user.getName());
        }
        return user;
    }

    public List<User> getFriendsById(long id) {
        if (userStorage.getUserById(id) == null) {
            log.error("Указан некорректныйй id пользователя.");
            throw new InvalidIdUserException("Указан некорректный id пользователя.");
        }

        User user = userStorage.getFriendsById(id);
        Set<Long> friendsId = user.getFriends(); // получили id всех друзей
        final List<User> friendsUser = new ArrayList<>();

        for (Long friends : friendsId) {
            User userFriend = userStorage.getFriendsById(friends);
            friendsUser.add(userFriend);
        }

        log.info("Запрошен список друзей пользователя {}.", user.getName());
        return friendsUser;
    }

    public List<User> getCommonFriends(long id, long otherId) { // общие друзья
        User user = userStorage.getFriendsById(id);
        Set<Long> usersId = user.getFriends(); // получили id всех друзей

        User otherUser = userStorage.getFriendsById(otherId);
        Set<Long> otherUsersId = otherUser.getFriends(); // получили id всех других друзей

        final Set<Long> commonUsersId = usersId.stream() // общие элементы двух списков
                .filter(otherUsersId::contains)
                .collect(Collectors.toSet()); // нагуглил свой первый стрим =)

        final List<User> commonUsers = new ArrayList<>();

        for (Long friends : commonUsersId) {
            User commonFriend = userStorage.getFriendsById(friends);
            commonUsers.add(commonFriend);
        }
        log.info("Запрошен общий список друзей пользователей {} и {}.", user.getName(), otherUser.getName());
        return commonUsers;
    }
}
