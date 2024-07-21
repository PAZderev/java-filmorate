package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa addMpa(Mpa mpa);

    Mpa getMpa(long id);

    Mpa updateMpa(Mpa mpa);

    Collection<Mpa> getAllMpas();

    void deleteMpa(long id);
}
