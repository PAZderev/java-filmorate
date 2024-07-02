package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film);

    Film getFilm(long id);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    void deleteFilm(long id);
}
