package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NonNull
    @Setter
    private  int id;
    @Email
    @NotBlank
    @NonNull
    private  String email;
    @NotBlank
    @NonNull
    private  String login;
    private  String name;
    @NonNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE )
    private LocalDate birthday;
}

