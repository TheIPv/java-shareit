package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    private Long id;
    private String text;
    @ManyToOne()
    @JoinColumn(name = "items_id", referencedColumnName = "id")
    private Item item;
    @ManyToOne()
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User author;
    private LocalDateTime created;
}
