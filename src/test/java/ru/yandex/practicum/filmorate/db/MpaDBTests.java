package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.classes.db.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class MpaDBTests {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void addMpaTest() {
        Mpa mpa = new Mpa();
        mpa.setName("test");
        mpaDbStorage.addMpa(mpa);
        assertThat(mpaDbStorage.getMpa(mpa.getId()).getName()).isEqualTo("test");
    }

    @Test
    public void deleteMpaTest() {
        Mpa mpa = new Mpa();
        mpa.setName("test1");
        mpaDbStorage.addMpa(mpa);
        mpaDbStorage.deleteMpa(mpa.getId());
        assertThrows(NotFoundException.class, () -> mpaDbStorage.getMpa(mpa.getId()));
    }

    @Test
    public void getMpaTest() {
        assertThat(mpaDbStorage.getMpa(1)).isNotNull();
    }

    @Test
    public void getAllMpasTest() {
        assertThat(mpaDbStorage.getAllMpas()).hasSizeGreaterThan(1);
    }

    @Test
    public void updateMpaTest() {
        Mpa mpa = new Mpa();
        mpa.setName("test2");
        mpaDbStorage.addMpa(mpa);
        mpa.setName("test3");
        mpaDbStorage.updateMpa(mpa);
        assertThat(mpaDbStorage.getMpa(mpa.getId()).getName()).isEqualTo("test3");
    }
}
