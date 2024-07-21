package ru.yandex.practicum.filmorate.storage.classes.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.List;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre ORDER BY ID";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String DELETE_GENRE_BY_ID_QUERY = "DELETE FROM genre WHERE id = ?";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO genre (name) VALUES (?)";
    private static final String UPDATE_GENRE_QUERY = "UPDATE genre SET name = ? WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper, PlatformTransactionManager transactionManager) {
        super(jdbc, mapper, transactionManager);
    }

    @Override
    public Genre getGenre(long id) {
        Genre genre = findOne(FIND_GENRE_BY_ID_QUERY, id).orElse(null);
        if (genre == null) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void deleteGenre(long id) {
        delete(DELETE_GENRE_BY_ID_QUERY, id);
    }

    @Override
    public Genre updateGenre(Genre genre) {
        Genre genreToUpdate = getGenre(genre.getId());
        update(UPDATE_GENRE_QUERY, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Genre addGenre(Genre genre) {
        long id = insert(INSERT_GENRE_QUERY, genre.getName());
        genre.setId(id);
        return genre;
    }
}
