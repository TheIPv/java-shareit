
package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @Id
    private Long id;
    private String name;
    @NotNull
    @Email
    private String email;
}