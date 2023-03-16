package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID) Long bookerId,
                                 @Valid @RequestBody BookingDtoCreate bookingDtoCreate) {
        log.info("Получен POST-запрос к эндпоинту: '/bookings' +" +
                " на создание бронирования от пользователя с ID={}", bookerId);
        return bookingService.addBooking(bookingDtoCreate, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApprove(@PathVariable Long bookingId,
                                    @RequestHeader(USER_ID) Long bookerId,
                                    @RequestParam Boolean approved) {
        return bookingService.setApprove(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader(USER_ID) Long bookerId) {
        return bookingService.getBooking(bookingId, bookerId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader(USER_ID) Long userId) {
            return bookingService.getUserBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemsBookings(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader(USER_ID) Long userId) {
        return bookingService.getUserItemsBookings(state, userId);
    }

}
