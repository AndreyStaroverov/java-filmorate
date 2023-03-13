package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    @Setter
    private  int id;
    @Email
    @NotBlank
    private  String email;
    @NotBlank
    private  String login;
    private  String name;
    @NonNull
    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE )
    private LocalDate birthday;
}

