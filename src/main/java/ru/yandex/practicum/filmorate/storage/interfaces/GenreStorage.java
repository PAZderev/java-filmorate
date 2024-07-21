package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre addGenre(Genre genre);

    Genre getGenre(long id);

    Genre updateGenre(Genre genre);

    Collection<Genre> getAllGenres();

    void deleteGenre(long id);
}
