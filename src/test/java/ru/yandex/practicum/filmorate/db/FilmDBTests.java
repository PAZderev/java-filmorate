package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.classes.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.classes.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.classes.db.MpaDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class FilmDBTests {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.getFilm(1);
        assertThat(film).isNotNull();
        assertThat(film.getId()).isEqualTo(1);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        assertThat(films).isNotNull();
        assertThat(films.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film();
        film.setName("Test film");
        film.setDescription("Test film description");
        film.setDuration(10);
        film.setMpa(mpaDbStorage.getMpa(1));
        film.setReleaseDate(LocalDate.ofYearDay(2020,1));
        film.setUsersLiked(Set.of((long) 1));
        film = filmStorage.addFilm(film);
        filmStorage.deleteFilm(film.getId());
        Film finalFilm = film;
        assertThrows(NotFoundException.class, () -> filmStorage.getFilm(finalFilm.getId()));

    }

    @Test
    public void testSaveFilm() {
        Film film = new Film();
        film.setName("Test film");
        film.setDescription("Test film description");
        film.setDuration(10);
        film.setMpa(mpaDbStorage.getMpa(1));
        film.setReleaseDate(LocalDate.ofYearDay(2020,1));
        film.setUsersLiked(Set.of((long) 1));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getFilm(film.getId())).isNotNull().isEqualTo(film);
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmStorage.getFilm(1);
        film.setName("Test film");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilm(1)).isNotNull().isEqualTo(film);
    }

}
