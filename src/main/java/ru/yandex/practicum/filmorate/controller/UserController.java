package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")  // получить пользователя по id
    public User getUserById(
            @PathVariable("id") String id) {
        return userService.getUserById(Integer.parseInt(id));
    }

    @PutMapping("/{id}/friends/{friendId}")  // добавить пользователя в друзья
    public User addToFriends(
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId) {
        return userService.addToFriends(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // удалить пользователя из друзей
    public User removeFromFriends(
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId) {
        return userService.removeFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @GetMapping("/{id}/friends")  // получить друзей пользователя по id
    public List<User> getFriendsById(
            @PathVariable("id") String id) {
        return userService.getFriendsById(Integer.parseInt(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")  // получить список друзей, общих с другим пользователем
    public List<User> getCommonFriends(
            @PathVariable("id") String id,
            @PathVariable("otherId") String otherId) {
        return userService.getCommonFriends(Integer.parseInt(id), Integer.parseInt(otherId));
    }
}