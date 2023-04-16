package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    public MpaController(@Autowired MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public Collection<Mpa> mpaAll() {
        log.debug("Попытка получить список mpa...");
        return mpaService.findAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa filmsAll(@PathVariable("id") Long id) {
        log.debug("Попытка получить mpa по идентификатору");
        return mpaService.getMpaById(id);
    }
}
