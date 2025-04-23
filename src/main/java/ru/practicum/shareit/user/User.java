package ru.practicum.shareit.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String email;
}
