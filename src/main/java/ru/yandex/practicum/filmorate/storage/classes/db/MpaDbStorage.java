package ru.yandex.practicum.filmorate.storage.classes.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper, PlatformTransactionManager transactionManager) {
        super(jdbc, mapper, transactionManager);
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY ID";
    private static final String FIND_MPA_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
    private static final String DELETE_MPA_BY_ID_QUERY = "DELETE FROM mpa WHERE id = ?";
    private static final String INSERT_MPA_QUERY = "INSERT INTO mpa (name) VALUES (?)";
    private static final String UPDATE_MPA_QUERY = "UPDATE mpa SET name = ? WHERE id = ?";


    @Override
    public Mpa getMpa(long id) {
        Mpa mpa = findOne(FIND_MPA_BY_ID_QUERY, id).orElse(null);
        if (mpa == null) {
            throw new NotFoundException("Mpa with id " + id + " not found");
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpas() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void deleteMpa(long id) {
        delete(DELETE_MPA_BY_ID_QUERY, id);
    }

    @Override
    public Mpa updateMpa(Mpa mpa) {
        Mpa mpaToUpdate = getMpa(mpa.getId());
        update(UPDATE_MPA_QUERY, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public Mpa addMpa(Mpa mpa) {
        long id = insert(INSERT_MPA_QUERY, mpa.getName());
        mpa.setId(id);
        return mpa;
    }
}
