package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.vakidations.FilmDateApprove;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {

    @Setter
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE )
    @FilmDateApprove
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final Set<Long> likes = new HashSet<>();
}
