package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewItemRequest {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
}
