package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {
    @Length(max = 512)
    @NotNull
    @NotBlank
    @NotEmpty
    private String text;
}
