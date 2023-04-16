package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@Slf4j
public class GenreController {

    private final GenreService genreService;

    public GenreController(@Autowired GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public Collection<Genre> filmsAll() {
        log.debug("Попытка получить список пользователей...");
        return genreService.findAll();
    }

    @GetMapping("/genres/{id}")
    public Genre filmsAll(@PathVariable("id") Long id) {
        log.debug("Попытка получить genre по идентификатору");
        return genreService.getGenreById(id);
    }
}
