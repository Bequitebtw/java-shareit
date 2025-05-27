package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class NewCommentRequest {
    @Length(max = 512)
    @NotNull
    private String text;
}
