package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.classes.db.GenreDbStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class GenreDBTests {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void findGenreByIdTest() {
        assertThat(genreDbStorage.getGenre(1)).isNotNull();
    }

    @Test
    public void findAllGenresTest() {
        assertThat(genreDbStorage.getAllGenres()).isNotNull().isNotEmpty().hasSizeGreaterThan(1);
    }

    @Test
    public void saveGenreTest() {
        Genre genre = new Genre();
        genre.setName("test");
        genreDbStorage.addGenre(genre);
        assertThat(genreDbStorage.getGenre(genre.getId())).isNotNull();
    }

    @Test
    public void deleteGenreTest() {
        Genre genre = new Genre();
        genre.setName("test1");
        genreDbStorage.addGenre(genre);
        genreDbStorage.deleteGenre(genre.getId());
        assertThrows(NotFoundException.class, () -> genreDbStorage.getGenre(genre.getId()));
    }

    @Test
    public void updateGenreTest() {
        Genre genre = new Genre();
        genre.setName("testUpdate");
        genreDbStorage.addGenre(genre);
        genre.setName("testUpdate2");
        genreDbStorage.updateGenre(genre);
        assertThat(genreDbStorage.getGenre(genre.getId()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "testUpdate2");
    }
}
