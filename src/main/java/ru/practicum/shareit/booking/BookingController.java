package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
                                            @RequestHeader(USER_ID) Long userId,
                                            @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                            @Min(1) @RequestParam(required = false, defaultValue = "20") int size) {
            return bookingService.getUserBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemsBookings(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader(USER_ID) Long userId,
                                            @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                            @Min(1) @RequestParam(required = false, defaultValue = "20") int size) {
        return bookingService.getUserItemsBookings(state, userId, from, size);
    }

}
