package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.user.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int userId = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // создание пользователя - POST
    @PostMapping
    public User addUser(@RequestBody User user) {
        if (users.containsValue(user)) {
            log.error("Пользователь с логином \"{}\" был создан ранее.", user.getLogin());
            throw new CheckingDuplicateUserException("Пользователь с логином \"" + user.getLogin() + "\" был создан ранее.");
        } else if (user.getEmail() == null || (!user.getEmail().contains("@"))) { // пусто или не содержит @
            log.error("Электронная почта не может быть пустой и должна содержать символ \"@\". {}", user);
            throw new InvalidUserEmailException("Электронная почта не может быть пустой и должна содержать символ \"@\".");
        } else if (user.getLogin() == null
                || user.getLogin().isBlank()        // isBlank - пустой ли?
                || user.getLogin().contains(" ")) { // или содержит пробелы
            log.error("Логин пользователя не может быть пустым или содержать пробелы. {}", user);
            throw new InvalidUserLoginException("Логин пользователя не может быть пустым или содержать пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) { // isAfter - позже
            log.error("Дата рождения пользователя не может быть в будущем. {}", user);
            throw new InvalidUserBirthdayException("Дата рождения пользователя не может быть в будущем.");
        } else if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin());
            return getUser(user);
        } else {
            return getUser(user);
        }
    }

    private User getUser(@RequestBody User user) {
        user.setId(userId);
        users.put(userId, user);
        userId++;
        log.info("Пользователь \"{}\" c id \"{}\" успешно создан.", user.getName(), user.getId());
        System.out.println("Пользователь \"" + user.getName() + "\" c id \"" + user.getId() + "\" успешно создан.");
        return user;
    }

    // обновление пользователя - PUT
    @PutMapping
    public User addOrUpdateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Невозможно обновить данные о пользователе. Пользователь отсутствует в базе. {}", user);
            throw new InvalidUserException("Невозможно обновить данные о пользователе. Пользователь отсутствует в базе.");
        } else {
            users.put(user.getId(), user);
            log.info("Пользователь c id \"{}\" успешно обновлен.", user.getId());
            System.out.println("Пользователь c id \"" + user.getId() + "\" успешно обновлен.");
            return user;
        }
    }

    // получение списка всех пользователей - GET
    @GetMapping
    public List<User> getUsers() {
        System.out.println("Получен список всех пользователей.");
        return new ArrayList<>(users.values());
    }
}
