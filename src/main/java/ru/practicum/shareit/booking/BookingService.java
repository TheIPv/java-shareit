package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoCreate bookingDtoCreate, Long bookerId);

    BookingDto setApprove(Long bookingId, Long bookerId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long bookerId);

    List<BookingDto> getUserBookings(String state, Long userId);

    List<BookingDto> getUserItemsBookings(String state, Long userId);
}
