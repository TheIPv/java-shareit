package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long ownerId, ItemDto item);

    ItemDto updateItem(Long userId, ItemDto item);

    ItemDto getItemById(Long itemId, Long requestorId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItemByText(String search);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);
}
