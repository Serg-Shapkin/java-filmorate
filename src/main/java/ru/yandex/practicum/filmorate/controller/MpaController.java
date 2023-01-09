package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.mpa.IncorrectMpaIdException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.mpa.MpaServiceImpl;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaServiceImpl mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Rating> getAllRating() {
        return mpaService.getAllRating();
    }

    @GetMapping("/{id}")
    public Rating getRatingById(
            @PathVariable("id") Integer id) {
        if (mpaService.getRatingById(id) == null) {
            throw new IncorrectMpaIdException("Указан некорректный id рейтинга");
        }
        return mpaService.getRatingById(id);
    }
}
