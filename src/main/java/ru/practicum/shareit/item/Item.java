package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.User;

import java.util.List;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 255)
    private String name;
    @Length(max = 512)
    private String description;
    private boolean available;
    @Length(max = 255)
    private String request;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "item")
    List<Comment>comments;
}
