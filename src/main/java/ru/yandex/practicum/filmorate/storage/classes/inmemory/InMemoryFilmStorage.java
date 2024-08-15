package ru.yandex.practicum.filmorate.storage.classes.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        log.info("Add film: {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @Override
    public Film getFilm(long id) {
        log.debug("Get film with id: {}", id);
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Update film: {}", film);
        if (film.getId() == null) {
            log.error("Film id is null");
            throw new ValidationException("Film id is null");
        }
        if (!films.containsKey(film.getId())) {
            log.error("Film not found");
            throw new NotFoundException("Film not found");
        }
        films.put(film.getId(), film);
        log.info("Film updated: {}", film);
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        log.info("Delete film: {}", id);
        films.remove(id);
        log.info("Film deleted: {}", id);

    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
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
