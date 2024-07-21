package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.login.LoginAnnotation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotNull(message = "Email must be not null")
    @Email(message = "Email not valid")
    private String email;
    private String name;
    @NotNull(message = "Login must be not null")
    @NotBlank(message = "Login must be not blank")
    @LoginAnnotation
    private String login;
    @PastOrPresent(message = "Birthday must be past or present")
    private LocalDate birthday;
    private Set<Long> sentFriendRequests = new HashSet<>();
    private Set<Long> receivedFriendRequests = new HashSet<>();
}
