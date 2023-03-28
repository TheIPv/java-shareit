package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    public void createItem() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(userId, "ItemName", "ItemDescription", true, null, userId));
        ItemDto itemDtoTest = itemService.addItem(userId, itemDto);

        assertThat("ItemName", equalTo(itemDtoTest.getName()));
        assertThat("ItemDescription", equalTo(itemDtoTest.getDescription()));
    }

    @Test
    public void updateItemName() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.addItem(userId, itemDto).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemNameUpdate", null, true, null, userId));
        itemDtoUpdate.setId(itemId);

        ItemDto itemDtoTest = itemService.updateItem(userId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemNameUpdate", equalTo(itemDtoTest.getName()));
    }

    @Test
    public void updateItemDescription() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.addItem(userId, itemDto).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemName", "ItemDescriptionUpdate", true, null, userId));
        itemDtoUpdate.setId(itemId);
        ItemDto itemDtoTest = itemService.updateItem(userId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemDescriptionUpdate", equalTo(itemDtoTest.getDescription()));
    }

    @Test
    public void updateItemAvailable() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.addItem(userId, itemDto).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, null, null, false, null, userId));
        ItemDto itemDtoTest = itemService.updateItem(userId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat(false, equalTo(itemDtoTest.getAvailable()));
    }

    @Test
    public void updateItemNotUserId() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.addUser(userDto1).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.addItem(userId, itemDto).getId();
        itemDto.setId(itemId);

        assertThatThrownBy(() -> {
            itemService.updateItem(userId1, itemDto);
        }).isInstanceOf(NoSuchItemException.class);
    }

    @Test
    public void search() {
        List<ItemDto> items = itemService.searchItemByText("descrip", 0, 1);
        assertThat(0, equalTo(items.size()));
    }

    @Test
    public void itemTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L,"Description", user, LocalDateTime.now());
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Name");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(itemRequest.getId());

        Item item1 = itemMapper.toItem(itemDto, user);

        assertThat(itemDto, equalTo(ItemMapper.toItemDto(item1)));
    }

    @Test
    public void commentTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L,"Description", user, LocalDateTime.now());
        Item item = new Item(1L, "Name", "Description", true, itemRequest.getRequestor(), 1L);
        Comment comment = new Comment(1L, "Text", item, user, LocalDateTime.now());
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Text");
        commentDto.setId(1L);

        assertThat(comment, equalTo(commentDto));
        assertThat(comment.hashCode(), equalTo(commentDto.hashCode()));
    }

    @Test
    public void searchPage() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        ItemDto itemDto1 = itemMapper.toItemDto(new Item(0L, "ItemName", "Description", true, null, userId));
        Long itemId1 = itemService.addItem(userId, itemDto1).getId();

        List<ItemDto> items = itemService.searchItemByText("descrip", 1, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId1, equalTo(items.get(0).getId()));
    }
}
