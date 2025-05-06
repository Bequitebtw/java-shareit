package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByUserId(long userId);
    List<Item> findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase
            (String nameText, String descriptionText);
}
