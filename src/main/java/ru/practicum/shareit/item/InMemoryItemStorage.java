package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.exception.NoSuchItemException;

import java.util.LinkedList;
import java.util.List;
@Component("InMemoryItemStorage")
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private Long id = Long.valueOf(1);
    private List<Item> items = new LinkedList<>();

    @Override
    public Item addItem(Item item) {
        item.setId(id);
        ++id;
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        for (Item currentItem: items) {
            if (currentItem.getId() == item.getId()) {
                if (item.getOwner() == currentItem.getOwner()) {

                    if (item.getAvailable() != null) {
                        currentItem.setAvailable(item.getAvailable());
                    }
                    if (item.getDescription() != null) {
                        currentItem.setDescription(item.getDescription());
                    }
                    if (item.getOwner() != null) {
                        currentItem.setOwner(item.getOwner());
                    }
                    if (item.getRequestId() != null) {
                        currentItem.setRequestId(item.getRequestId());
                    }
                    if (item.getName() != null) {
                        currentItem.setName(item.getName());
                    }
                    return currentItem;
                }
                throw new NoAccessException("User with ID " + item.getOwner().getId() +
                        " isn't owner of item with ID " + item.getId());
            }
        }
        throw new NoSuchItemException("Item with ID " + item.getId() + " doesn't exist");
    }

    @Override
    public Item getItemById(Long itemId) {
        for (Item currentItem : items) {
            if (currentItem.getId() == itemId) {
                return currentItem;
            }
        }
        throw new NoSuchItemException("Item with ID " + itemId + " wasn't found");
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        List<Item> userItems = new LinkedList<>();
        for (Item currentItem : items) {
            if (currentItem.getOwner().getId() == userId) {
                userItems.add(currentItem);
            }
        }
        return userItems;
    }

    @Override
    public List<Item> searchItemByText(String search) {
        if (StringUtils.isEmpty(search)) {
            return new LinkedList<>();
        }
        List<Item> foundedItems = new LinkedList<>();
        for (Item currentItem: items) {
            if (StringUtils.containsIgnoreCase(currentItem.getName(), search) ||
                StringUtils.containsIgnoreCase(currentItem.getDescription(), search)) {
                foundedItems.add(currentItem);
            }
        }
        return foundedItems;
    }
}
