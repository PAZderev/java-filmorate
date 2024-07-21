package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(long id) {
        log.info("Get mpa by id: {}", id);
        return mpaStorage.getMpa(id);
    }

    public Collection<Mpa> getAllMpas() {
        log.info("Get all mpas");
        return mpaStorage.getAllMpas();
    }

    public Mpa addMpa(Mpa mpa) {
        log.info("Add mpa: {}", mpa);
        return mpaStorage.addMpa(mpa);
    }

    public Mpa updateMpa(Mpa mpa) {
        log.info("Update mpa: {}", mpa);
        return mpaStorage.updateMpa(mpa);
    }

    public void deleteMpa(long id) {
        log.info("Delete mpa: {}", id);
        mpaStorage.deleteMpa(id);
    }
}
