package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Entity(name = "Items")
@NoArgsConstructor
@AllArgsConstructor
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