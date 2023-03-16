package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> searchItemByText(String search);

}
