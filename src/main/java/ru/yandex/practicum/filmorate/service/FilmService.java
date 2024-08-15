package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelOperationException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier FilmStorage filmStorage, @Qualifier UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLikeToFilm(long filmId, long userId) {
        log.info("Adding like to film {} by user {}", filmId, userId);
        Film filmToBeLiked = filmStorage.getFilm(filmId);
        User currentUser = userStorage.getUser(userId);
        validateFilmAndUser(filmToBeLiked, currentUser);

        if (filmToBeLiked.getUsersLiked().contains(userId)) {
            log.error("Film {} already liked by user {}", filmToBeLiked, currentUser);
            throw new ModelOperationException("Film already liked");
        }
        filmToBeLiked.getUsersLiked().add(userId);
        filmStorage.updateFilm(filmToBeLiked);
        log.info("Film {} liked by user {}", filmToBeLiked, currentUser);
        return filmToBeLiked;
    }

    public boolean deleteLikeFromFilm(long filmId, long userId) {
        log.info("Deleting like from film {} by user {}", filmId, userId);
        Film filmToBeDisliked = filmStorage.getFilm(filmId);
        User currentUser = userStorage.getUser(userId);
        validateFilmAndUser(filmToBeDisliked, currentUser);
        if (!filmToBeDisliked.getUsersLiked().contains(userId)) {
            log.error("Film {} not liked by user {}", filmToBeDisliked, currentUser);
            throw new ModelOperationException("Film not liked by user");
        }
        filmToBeDisliked.getUsersLiked().remove(userId);
        filmStorage.updateFilm(filmToBeDisliked);
        log.info("Deleted like from film {} by {}", filmToBeDisliked, currentUser);
        return true;
    }

    public Collection<Film> getTopFilms(int count) {
        log.info("Getting top 10 films");
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getUsersLiked().size() - film1.getUsersLiked().size())
                .limit(count)
                .toList();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilm(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public boolean deleteFilm(long id) {
        filmStorage.deleteFilm(id);
        return true;
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    private void validateFilmAndUser(Film film, User user) {
        log.debug("Validating film {} and user {}", film, user);
        if (film == null) {
            log.error("Film does not exist");
            throw new NotFoundException("Film does not exist");
        }
        if (user == null) {
            log.error("User does not exist");
            throw new NotFoundException("User does not exist");
        }
    }


}
