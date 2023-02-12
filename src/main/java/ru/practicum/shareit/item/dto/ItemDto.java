package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
}
