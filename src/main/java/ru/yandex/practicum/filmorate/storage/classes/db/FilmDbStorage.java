package ru.yandex.practicum.filmorate.storage.classes.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@Qualifier
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM film";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
    private static final String FIND_USERS_LIKED_QUERY = "SELECT user_id FROM users_likes WHERE film_id = ?";
    private static final String FIND_GENRES_BY_FILM_QUERY = "SELECT genre_id FROM genre_film WHERE film_id = ?";

    private static final String INSERT_FILM_QUERY = "INSERT INTO film (name, description, release_date, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_USERS_LIKES_QUERY = "INSERT INTO users_likes (film_id, user_id) VALUES (?, ?)";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO genre_film (film_id, genre_id)" +
            " VALUES (?, ?)";

    private static final String UPDATE_FILM_QUERY = "UPDATE film SET" +
            " name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";


    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String DELETE_USERS_LIKES_QUERY = "DELETE FROM users_likes WHERE film_id = ? and user_id = ?";
    private static final String DELETE_GENRE_FILM_QUERY = "DELETE FROM genre_film WHERE film_id = ? and genre_id = ?";

    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, PlatformTransactionManager transactionManager,
                         GenreStorage genreStorage, MpaStorage mpaStorage) {
        super(jdbc, mapper, transactionManager);
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film addFilm(Film film) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            long filmId = insert(INSERT_FILM_QUERY,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
            for (Long userId : film.getUsersLiked()) {
                update(INSERT_USERS_LIKES_QUERY, filmId, userId);
            }

            for (Genre genre : film.getGenres()) {
                update(INSERT_FILM_GENRE_QUERY, filmId, genre.getId());
            }

            transactionManager.commit(status);
            film.setId(filmId);
            return film;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new InternalServerException("Не удалось сохранить фильм + " + film + ":\n" + e.getMessage());
        }
    }

    @Override
    public Film getFilm(long id) {
        Optional<Film> optional = findOne(FIND_BY_ID_QUERY, id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
        Film film = optional.get();
        fillMpaField(film);
        fillGenresField(film);
        fillUsersLikedField(film);
        return film;
    }

    private void fillMpaField(Film film) {
        Mpa mpaWithoutName = film.getMpa();
        film.setMpa(mpaStorage.getMpa(mpaWithoutName.getId()));
    }

    private void fillUsersLikedField(Film film) {
        Set<Long> usersLiked = new HashSet<>(jdbc.queryForList(FIND_USERS_LIKED_QUERY, Long.class, film.getId()));
        film.setUsersLiked(usersLiked);

    }

    private void fillGenresField(Film film) {
        Set<Long> genreIds = new HashSet<>(jdbc.queryForList(FIND_GENRES_BY_FILM_QUERY, Long.class, film.getId()));
        Set<Genre> genres = genreIds.stream()
                .map(genreStorage::getGenre)
                .collect(Collectors.toSet());
        film.setGenres(genres);
    }


    @Override
    public Film updateFilm(Film film) {
        long totalRowsUpdated = 0;
        Film filmToUpdate = getFilm(film.getId()); // Проверяет, что фильм с таким id существует
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            totalRowsUpdated += updateFilmData(film);
            totalRowsUpdated += updateFilmGenreData(film);
            totalRowsUpdated += updateFilmUsersLikeData(film);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new InternalServerException("При обновлении фильма " + film + " возникла ошибка");
        }
        if (totalRowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return film;
    }


    private int updateFilmData(Film film) {
        return update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

    }

    private long updateFilmUsersLikeData(Film film) {
        long rowsUpdated = 0;
        Set<Long> newFilmLikes = film.getUsersLiked();
        Set<Long> currentFilmLikes = new HashSet<>(jdbc.queryForList(FIND_USERS_LIKED_QUERY, Long.class, film.getId()));
        for (Long userId : newFilmLikes) {
            if (!currentFilmLikes.contains(userId)) {
                rowsUpdated += update(INSERT_USERS_LIKES_QUERY, film.getId(), userId);
            }
        }

        for (Long userId : currentFilmLikes) {
            if (!newFilmLikes.contains(userId)) {
                rowsUpdated += delete(DELETE_USERS_LIKES_QUERY, film.getId(), userId);
            }
        }
        return rowsUpdated;
    }

    private long updateFilmGenreData(Film film) {
        long rowsUpdated = 0;
        Set<Long> newFilmGenres = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        Set<Long> currentFilmGenres = new HashSet<>(jdbc.queryForList(FIND_GENRES_BY_FILM_QUERY, Long.class, film.getId()));
        for (Long genreId : newFilmGenres) {
            if (!currentFilmGenres.contains(genreId)) {
                rowsUpdated += update(INSERT_FILM_GENRE_QUERY, film.getId(), genreId);
            }
        }

        for (Long genreId : currentFilmGenres) {
            if (!newFilmGenres.contains(genreId)) {
                rowsUpdated += delete(DELETE_GENRE_FILM_QUERY, film.getId(), genreId);
            }
        }

        return rowsUpdated;
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        for (Film film : films) {
            fillMpaField(film);
            fillGenresField(film);
            fillUsersLikedField(film);
        }
        return films;
    }

    @Override
    public void deleteFilm(long id) {
        delete(DELETE_QUERY, id);
    }
}
