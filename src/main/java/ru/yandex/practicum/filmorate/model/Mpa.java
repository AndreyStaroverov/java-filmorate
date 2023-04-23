package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Mpa {
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String name;
}
