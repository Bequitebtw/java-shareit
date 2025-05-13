package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Data
public class ItemRequestDto {
    private long id;
    @NotNull
    private String description;
    private User requester;
    private LocalDateTime created = LocalDateTime.now();
}
