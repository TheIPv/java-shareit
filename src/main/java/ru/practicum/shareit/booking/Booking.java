package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Entity(name = "bookings")
public class Booking {
    @Id
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "items_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;
}
