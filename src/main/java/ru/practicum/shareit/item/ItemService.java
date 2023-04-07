package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long ownerId, ItemDto item);

    ItemDto updateItem(Long userId, ItemDto item);

    ItemDto getItemById(Long itemId, Long requestorId);

    List<ItemDto> getUserItems(Long userId, int from, int size);

    List<ItemDto> searchItemByText(String search, int from, int szie);

    List<ItemDto> getItemsByRequestId(Long itemRequestId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);
}
