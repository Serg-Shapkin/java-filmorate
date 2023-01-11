package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface MpaService {
    List<Rating> getAll();
    Rating getById(Integer id);
}
