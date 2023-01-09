package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private int id;
    private String name;

    @NotNull
    @NotBlank
    private String login;

    @Email
    private String email;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

}