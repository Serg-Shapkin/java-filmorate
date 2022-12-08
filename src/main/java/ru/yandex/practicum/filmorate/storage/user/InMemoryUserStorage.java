package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    public final HashMap<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public void addToFriends(long id, long friendId) {
        User user = users.get(id);
        user.getFriends().add(friendId);
    }

    @Override
    public void removeFromFriends(long id, long friendId) {
        User user = users.get(id);
        user.getFriends().remove(friendId);
    }

    @Override
    public User getFriendsById(long id) {
        return users.get(id); // вернули друга, у которого запрашивем список друзей
    }
}