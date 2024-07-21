package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(long id) {
        log.info("Get genre by id: {}", id);
        return genreStorage.getGenre(id);
    }

    public Collection<Genre> getAllGenres() {
        log.info("Get all genres");
        return genreStorage.getAllGenres();
    }

    public Genre addGenre(Genre genre) {
        log.info("Add genre: {}", genre);
        return genreStorage.addGenre(genre);
    }

    public Genre updateGenre(Genre genre) {
        log.info("Update genre: {}", genre);
        return genreStorage.updateGenre(genre);
    }

    public void deleteGenre(long id) {
        log.info("Delete genre by id: {}", id);
        genreStorage.deleteGenre(id);
    }
}
