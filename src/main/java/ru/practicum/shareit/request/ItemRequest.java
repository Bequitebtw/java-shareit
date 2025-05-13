package ru.practicum.shareit.request;

import jakarta.persistence.Table;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Table(name = "requests")
@Data
public class ItemRequest {
    private long id;
    private String description;
    private User requester;
    private LocalDateTime created = LocalDateTime.now();
}
