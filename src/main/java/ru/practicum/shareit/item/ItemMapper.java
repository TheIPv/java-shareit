package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    @Autowired
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }
    public static Item toItem(ItemDto itemDto, User owner, Long requestId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                requestId
        );
    }
}
