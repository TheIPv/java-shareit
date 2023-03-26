package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {
    private static ItemService itemService;
    @Autowired
    public ItemRequestMapper(ItemService itemService) {
        this.itemService = itemService;
    }
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        List<ItemDto> items = itemService.getItemsByRequestId(itemRequest.getId());
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setRequestor(UserMapper.toUserDto(itemRequest.getRequestor()));
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setRequestor(UserMapper.toUser(itemRequestDto.getRequestor()));
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setDescription(itemRequestDto.getDescription());

        return itemRequest;
    }
}
