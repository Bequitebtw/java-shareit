package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    @Length(max = 512)
    @NotNull
    private String text;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "item_id")
    private Item item;
    @JoinColumn
    @JsonIgnore
    @ManyToOne
    private User author;
}
