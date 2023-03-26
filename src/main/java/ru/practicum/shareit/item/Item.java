package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Entity(name = "Items")
@Table(name = "items")
@Data
public class Item {
    @Id
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User owner;
    @Column(name = "requests_id")
    private Long requestId;
}