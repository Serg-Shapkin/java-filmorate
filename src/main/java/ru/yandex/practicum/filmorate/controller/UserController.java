package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")  // получить пользователя по id
    public User getUserById(
            @PathVariable("id") Integer id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")  // добавить пользователя в друзья
    public User addToFriends(
            @PathVariable("id") Integer id,
            @PathVariable("friendId") Integer friendId) {
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // удалить пользователя из друзей
    public User removeFromFriends(
            @PathVariable("id") Integer id,
            @PathVariable("friendId") Integer friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")  // получить друзей пользователя по id
    public List<User> getFriendsById(
            @PathVariable("id") Integer id) {
        return userService.getFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")  // получить список друзей, общих с другим пользователем
    public List<User> getCommonFriends(
            @PathVariable("id") Integer id,
            @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}