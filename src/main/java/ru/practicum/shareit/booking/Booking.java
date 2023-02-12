package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime finish;
    private Item item;
    private User booker;
    private Statuses status;
}
