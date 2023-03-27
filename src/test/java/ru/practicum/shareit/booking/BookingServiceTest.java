package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    public void createBooking() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.addUser(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.addUser(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.addItem(userId1, itemDto).getId();

        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate();
        bookingDtoCreate.setStart(LocalDateTime.now().plusSeconds(2));
        bookingDtoCreate.setEnd(LocalDateTime.now().plusSeconds(4));
        bookingDtoCreate.setItemId(itemId);

        BookingDto booking = bookingService.addBooking(bookingDtoCreate, userId);

        assertThat(itemId, equalTo(booking.getItem().getId()));
        assertThat(userId, equalTo(booking.getBooker().getId()));
        assertThat(Status.WAITING, equalTo(booking.getStatus()));
    }

    @Test
    public void createBookingAvailableFalse() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.addUser(userDto).getId();

        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate();
        bookingDtoCreate.setStart(LocalDateTime.now());
        bookingDtoCreate.setEnd(LocalDateTime.now().plusNanos(2));

        assertThatThrownBy(() -> {
            bookingService.addBooking(bookingDtoCreate, userId);
        }).isInstanceOf(NotValidException.class);
    }
}