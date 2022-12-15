package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.user.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exception.user.UserDatabaseIsEmptyException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private final static Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    public final HashMap<Long, User> users = new HashMap<>();

    private long userId = 1;

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
    public Map<Long, User> getAllUsers() {
        if (users.isEmpty()) {
            log.error("В базе не сохранено ни одного пользователя");
            throw new UserDatabaseIsEmptyException("В базе не сохранено ни одного пользователя");
        }
        return users;
    }

    @Override
    public User getUserById(long id) {   // получить пользователя по id
        if (!users.containsKey(id)) {
            log.error("Указан некорректный id пользователя");
            throw new IncorrectUserIdException("Указан некорректный id пользователя"); // 404
        }
        log.info("Запрошен пользователь c id={}", id);
        return users.get(id);
    }
}