package ru.practicum.shareit.requests.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewItemRequest {
    @NotNull
    private String description;
}
