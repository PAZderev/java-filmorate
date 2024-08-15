package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> getAllMpas() {
        return mpaService.getAllMpas();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        return mpaService.getMpaById(id);
    }

    @PostMapping
    public Mpa addMpa(@RequestBody Mpa mpa) {
        return mpaService.addMpa(mpa);
    }

    @PutMapping
    public Mpa updateMpa(@RequestBody Mpa mpa) {
        return mpaService.updateMpa(mpa);
    }

    @DeleteMapping("/{id}")
    public void deleteMpa(@PathVariable long id) {
        mpaService.deleteMpa(id);
    }
}
