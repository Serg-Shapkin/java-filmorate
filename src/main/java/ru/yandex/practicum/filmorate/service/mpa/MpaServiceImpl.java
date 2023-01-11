package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.mpa.IncorrectMpaIdException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    @Override
    public List<Rating> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Rating getById(Integer id) {
        if (mpaStorage.getById(id) == null) {
            throw new IncorrectMpaIdException("Указан некорректный id рейтинга");
        }
        return mpaStorage.getById(id);
    }
}
