package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    private Set<Long> likes;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return name.equals(film.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
