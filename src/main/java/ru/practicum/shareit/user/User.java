package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Entity(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id")
    private Long id;
    private String name;
    @NotNull
    @Email
    private String email;
}
