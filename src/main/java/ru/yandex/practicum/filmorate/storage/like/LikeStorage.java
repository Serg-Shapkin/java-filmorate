package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void addLike(Integer id, Integer userId);
    void removeLike(Integer id, Integer userId);
}
