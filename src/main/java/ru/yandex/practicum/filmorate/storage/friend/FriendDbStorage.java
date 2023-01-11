package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

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
}
