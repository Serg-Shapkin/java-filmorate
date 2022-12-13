package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {

    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max=200)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Min(0)
    private int duration;
    private final Set<Long> likes = new HashSet<>();
}