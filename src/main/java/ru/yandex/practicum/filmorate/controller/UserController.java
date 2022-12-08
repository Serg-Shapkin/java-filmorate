package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")  // получить пользователя по id
    public User getUserById(
            @PathVariable("id") String id) {
        return userService.getUserById(Long.parseLong(id));
    }

    @PutMapping("/{id}/friends/{friendId}")  // добавить пользователя в друзья
    public User addToFriends(
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId) {
        return userService.addToFriends(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // удалить пользователя из друзей
    public User removeFromFriends(
            @PathVariable("id") String id,
            @PathVariable("friendId") String friendId) {
        return userService.removeFromFriends(Long.parseLong(id), Long.parseLong(friendId));
    }

    @GetMapping("/{id}/friends")  // получить друзей пользователя по id
    public List<User> getFriendsById(
            @PathVariable("id") String id) {
        return userService.getFriendsById(Long.parseLong(id));
    }

    @GetMapping("{id}/friends/common/{otherId}")  // получить списко друзей, общих с другим пользхователем
    public List<User> getCommonFriends(
            @PathVariable("id") String id,
            @PathVariable("otherId") String otherId) {
        return userService.getCommonFriends(Long.parseLong(id), Long.parseLong(otherId));
    }
}
