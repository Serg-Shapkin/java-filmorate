package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")  // получить фильм по id
    public Film getFilmById(
            @PathVariable("id") Integer id) {
        return filmService.getById(id);
    }

    @PutMapping("/{id}/like/{userId}") // id - какому фильму, userId - кто ставит
    public Film addLikeFilm(
            @PathVariable("id") Integer id,
            @PathVariable("userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeFilm(
            @PathVariable("id") Integer id,
            @PathVariable("userId") Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10") @Positive Integer count) {
        if (count < 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopular(count);
    }
}