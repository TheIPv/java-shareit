package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity(name = "comments")
@Table(name = "comments")
public class Comment {
    @Id
    private Long id;
    @NotBlank
    @NotEmpty
    private String text;
    @ManyToOne()
    @JoinColumn(name = "items_id", referencedColumnName = "id")
    private Item item;
    @ManyToOne()
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User author;
    private LocalDateTime created;
}
