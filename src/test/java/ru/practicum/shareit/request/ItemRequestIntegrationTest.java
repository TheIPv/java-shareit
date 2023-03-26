package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestController itemRequestController;
    private final ItemRequestDto itemRequestDtoOne = new ItemRequestDto(1L, "desc 1",
            new UserDto(), LocalDateTime.now(), List.of(new ItemDto()));

    @Test
    void whenCheckAddItemWithNonExistUser() {
        //test
        assertThrows(
                NoSuchItemException.class,
                ()->{
                    itemRequestController.addItemRequest(itemRequestDtoOne, 1L);
                });
    }
}