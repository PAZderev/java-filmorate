package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film: {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film: {}", film);
        if (film.getId() == null) {
            log.error("Film id is null");
            throw new ValidationException("Film id is null");
        }
        if (!films.containsKey(film.getId())) {
            log.error("Film not found");
            throw new ValidationException("Film not found");
        }
        films.put(film.getId(), film);
        log.info("Film updated: {}", film);
        return film;
    }

    private long getNextId() {
        long nextId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
