package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.genre.IncorrectGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
         return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(
            @PathVariable("id") Integer id) {
        if (genreService.getGenreById(id) == null) {
            throw new IncorrectGenreIdException("Указан некорректный id жанра");
        }
        return genreService.getGenreById(id);
    }
}
