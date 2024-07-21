package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.classes.inmemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.classes.inmemory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        validFilm = new Film();
        validFilm.setName("Spider Man");
        validFilm.setDuration(120);
        validFilm.setDescription("Test film");
        validFilm.setReleaseDate(LocalDate.of(2012, 1, 1));
    }

    @Test
    public void shouldReturnAllFilms() {
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    public void shouldAddValidFilm() {
        filmController.addFilm(validFilm);
        assertEquals(filmController.getAllFilms().size(), 1);
    }


    /*
    С помощью обычных Unit-тестов нельзя проверить аннотации @Valid, необходимо посылать запросы,
     что можно сделать с помощью mockMVC, но не думаю, что это задумывалось в спринте. Такие проверки сделаны в Postman.
     */

    @Test
    public void shouldUpdateFilm() {
        filmController.addFilm(validFilm);
        validFilm.setName("Update");
        filmController.updateFilm(validFilm);
        assertEquals(filmController.getAllFilms().size(), 1);
    }

    @Test
    public void shouldNotUpdateFilm() {
        filmController.addFilm(validFilm);
        Film film = new Film();
        assertThrows(ValidationException.class,
                () -> filmController.updateFilm(film));
    }
}
