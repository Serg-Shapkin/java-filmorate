package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exception.user.UserDatabaseIsEmptyException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UserDbStorage implements UserStorage {
    private final static Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private int userId = 0;
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        makeUserId();
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // если имя == null или пусто
            user.setName(user.getLogin()); // именем будет логин
        }
        userId++;
        user.setId(userId);
        jdbcTemplate.update("INSERT INTO USERS VALUES (?,?,?,?,?)",
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", user.getId());
        userRowSet.next();
        log.info("Пользователь {} успешно добавлен", user.getName());
        return makeUser(userRowSet);
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", user.getId());
        if (userRowSet.next()) {
            log.info("Данные пользователя {} успешно обновлены", user.getName());
            return makeUser(userRowSet);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");
        List<User> allUsersDb = new ArrayList<>();
        while (userRowSet.next()) {
            allUsersDb.add(makeUser(userRowSet));
        }
        if (allUsersDb.isEmpty()) {
            log.error("В базе не сохранено ни одного пользователя");
            throw new UserDatabaseIsEmptyException("В базе не сохранено ни одного пользователя");
        }
        log.info("Запрошены все пользователи из базы данных");
        return allUsersDb;
    }

    @Override
    public User getUserById(Integer id) {
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", id);
        userRowSet.next();
        if (userRowSet.last()) {
            log.info("Запрошен пользователь c id={}", id);
            return makeUser(userRowSet);
        } else {
            log.error("Указан некорректный id пользователя");
            throw new IncorrectUserIdException("Указан некорректный id пользователя");
        }
    }

    @Override
    public void addToFriends(Integer id, Integer friendId) {
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
        SqlRowSet friendRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
        if (userRowSet.next()) {
            if (friendRowSet.next()) {
                jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = true WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
                jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = true WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
            } else {
                jdbcTemplate.update("INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, TRUE)", id, friendId);
                jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = true WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
            }
        } else if (friendRowSet.next()) {
            jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = false WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
        } else {
            jdbcTemplate.update("INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, FALSE)", id, friendId);
        }
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
        SqlRowSet friendRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
        if (userRowSet.next()) {
            if (friendRowSet.next()) {
                jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
                jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = false WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
            } else {
                jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = false WHERE USER_ID = ? AND FRIEND_ID = ?", friendId, id);
            }
        } else if (friendRowSet.next()) {
            jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?", id, friendId);
        }
    }

    @Override
    public List<User> getFriendsById(Integer id) {
        List<User> friends = new ArrayList<>();
        SqlRowSet allFriendsRowSet = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?", id);
        while (allFriendsRowSet.next()) {
            friends.add(getUserById(allFriendsRowSet.getInt("FRIEND_ID")));
        }
        log.info("Запрошен список друзей пользователя {}", getUserById(id).getName());
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet commonFriendsRowSet = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM FRIENDSHIP  WHERE USER_ID = ? OR USER_ID = ? GROUP BY FRIEND_ID HAVING COUNT(*) > 1", id, otherId);
        while (commonFriendsRowSet.next()) {
            friends.add(getUserById(commonFriendsRowSet.getInt("FRIEND_ID")));
        }
        log.info("Запрошен общий список друзей друзей");
        return friends;
    }

    private User makeUser(SqlRowSet userRowSet) {
        User user = new User();
        user.setId(userRowSet.getInt("USER_ID"));
        user.setName(userRowSet.getString("NAME"));
        user.setLogin(userRowSet.getString("LOGIN"));
        user.setEmail(userRowSet.getString("EMAIL"));
        user.setBirthday(userRowSet.getDate("BIRTHDAY").toLocalDate());

        SqlRowSet userRowSetFriends = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?",
                userRowSet.getInt("USER_ID"));

        Set<Integer> friends = new HashSet<>();
        while (userRowSetFriends.next()) {
            friends.add(userRowSetFriends.getInt("FRIEND_ID"));
        }
        user.setFriends(friends);
        return user;
    }

    private void makeUserId() { // если в бд есть пользователи, то запоминаем максимальный id
        Integer userIdDb = jdbcTemplate.queryForObject("SELECT MAX(USER_ID) FROM USERS", Integer.class);
        if (userIdDb == null) {
            userId = 0;
        } else {
            userId = userIdDb;
        }
    }
}
