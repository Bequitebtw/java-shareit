package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Length(max = 255)
    @Column(nullable = false)
    private String name;
    @NotNull
    @Length(max = 521)
    @Column(nullable = false, unique = true)
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Почта не соответствует критериям")
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();
}
