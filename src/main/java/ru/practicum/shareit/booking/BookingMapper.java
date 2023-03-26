package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static BookingForItemDto toBookingForItemDto(Booking booking) {
        BookingForItemDto bookingForItemDto = new BookingForItemDto();

        bookingForItemDto.setId(booking.getId());
        bookingForItemDto.setStart(booking.getStart());
        bookingForItemDto.setEnd(booking.getEnd());
        bookingForItemDto.setBookerId(booking.getBooker().getId());

        return bookingForItemDto;
    }

    public static Booking toBookingFromDtoCreate(BookingDtoCreate bookingDtoCreate, Long id, User booker, Item item) {
        Booking booking = new Booking();

        booking.setId(id);
        booking.setStart(bookingDtoCreate.getStart());
        booking.setEnd(bookingDtoCreate.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);

        return booking;

    }
}
