package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                                      @RequestBody @Valid ItemDto itemDto) {
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId, @RequestBody ItemDto itemDto) {

        if (itemDto.getName() == null && itemDto.getDescription() == null && itemDto.getAvailable() == null) {
            throw new NotValidException("Error: all item fields is null");
        }
        System.out.println(" - Обновление вещи с ID = " + itemId + " владельца с ID = " + userId + ".");
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                      @PathVariable Long itemId) {
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Min(0) @RequestParam(name = "from", defaultValue = "0")
                                         Integer from,
                                         @Min(1) @RequestParam(name = "size", defaultValue = "10")
                                         Integer size) {
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(value = "text", required = false) String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from,
                                              @Min(1) @RequestParam(name = "size", defaultValue = "10")
                                              Integer size) {
        return itemClient.searchItemByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable Long itemId, @RequestBody @Valid CommentDto inputCommentDto) {
        return itemClient.createComment(userId, itemId, inputCommentDto);
    }
}
