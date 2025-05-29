package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Table(name = "requests")
@Entity
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User requester;
    @OneToMany(mappedBy = "request")
    private List<Item> items;
    private LocalDateTime created = LocalDateTime.now();
}
