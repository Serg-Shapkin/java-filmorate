package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Rating> getAllRating() {
        return mpaStorage.getAllRating();
    }

    @Override
    public Rating getRatingById(Integer id) {
        return mpaStorage.getRatingById(id);
    }

}
