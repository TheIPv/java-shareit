package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequest {
    @Id
    private Long id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User requestor;
    private LocalDateTime created;
}
