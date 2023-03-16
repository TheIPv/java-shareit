package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    @Override
    public ItemDto addItem(Long ownerId, ItemDto item) {
        if (ownerId == null) {
            throw new NotValidException("Owner ID not specified");
        }
        Item createdItem = itemMapper.toItem(item, userStorage.getUserById(ownerId), null);
        itemStorage.addItem(createdItem);
        return itemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto item) {
        if (userId == null) {
            throw new NotValidException("User ID not specified");
        }
        Item updatedItem = itemMapper.toItem(item, userStorage.getUserById(userId), null);
        updatedItem = itemStorage.updateItem(updatedItem);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemId == null) {
            throw new NotValidException("Item ID not specified");
        }
        return itemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        if (userId == null) {
            throw new NotValidException("User ID not specified");
        }
        return itemStorage.getUserItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemByText(String search) {
        return itemStorage.searchItemByText(search)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
