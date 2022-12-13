package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")  // получить фильм по id
    public Film getFilmById(
            @PathVariable("id") String id) {
        return filmService.getFilmById(Long.parseLong(id));
    }

    @PutMapping("/{id}/like/{userId}") // id - какому фильму, userId - кто ставит
    public Film addLikeFilm(
            @PathVariable("id") String id,
            @PathVariable("userId") String userId) {
        return filmService.addLikeFilm(Long.parseLong(id), Long.parseLong(userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeFilm(
            @PathVariable("id") String id,
            @PathVariable("userId") String userId) {
        return filmService.removeLikeFilm(Long.parseLong(id), Long.parseLong(userId));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) String count) {
        if (Long.parseLong(count) < 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopularFilms(Long.parseLong(count));
    }
}