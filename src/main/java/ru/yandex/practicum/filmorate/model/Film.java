package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.vakidations.FilmDateApprove;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Setter
    private int id;
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
}
