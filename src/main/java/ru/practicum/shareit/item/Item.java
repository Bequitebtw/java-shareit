package ru.practicum.shareit.item;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private String request;
}
