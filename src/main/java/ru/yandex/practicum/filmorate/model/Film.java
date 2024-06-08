package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.date.release.ReleaseDateAnnotation;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name must be not blank")
    private String name;
    @Size(max = 200, message = "Description length greater then 200")
    private String description;
    @ReleaseDateAnnotation
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive")
    private int duration;
}
