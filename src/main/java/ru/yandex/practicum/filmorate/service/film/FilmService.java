package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceImpl {
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(UserService userService) {
        this.userService = userService;
        this.filmStorage = new InMemoryFilmStorage();
    }

    @Override
    public Film addFilm(Film film){
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms().values());
    }

    @Override
    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    @Override
    public Film addLikeFilm(long id, long userId) {  // id - какому фильму, userId - кто ставит
        User user = userService.getUserById(userId);
        filmValidation(id);

        Film film = filmStorage.getAllFilms().get(id);
        film.getLikes().add(user.getId());

        filmStorage.getAllFilms().put(film.getId(), film);
        log.info("Пользователь {} поставил лайк фильму {}", user.getName(), film.getName());
        return film;
    }

    @Override
    public Film removeLikeFilm(long id, long userId) { // id - из какого фильма, userId - кто удаляет
        User user = userService.getUserById(userId);
        filmValidation(id);

        Film film = filmStorage.getAllFilms().get(id);
        film.getLikes().remove(user.getId());

        filmStorage.getAllFilms().put(film.getId(), film);
        log.info("Пользователь {} удалил лайк у фильма {}", user.getName(), film.getName());
        return film;
    }

    @Override
    public List<Film> getPopularFilms(long size) {
            List<Film> films = getAllFilms();

            FilmLikesComparator comparator = new FilmLikesComparator();
            films.sort(comparator);

            log.info("Запрошен список из {} фильмов",size);

            return films.stream()
                    .limit(size)
                    .collect(Collectors.toList());
    }

    private void filmValidation(long id) {
        if (!filmStorage.getAllFilms().containsKey(id)) {
            log.error("Фильм с id={} не найден в базе", id);
            throw new FilmValidationException("Фильм с id=" + id + " не найден в базе");
        }
    }
}