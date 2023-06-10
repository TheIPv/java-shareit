package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(value = "X-Sharer-User-Id") Long requesterId,
                                                 @RequestBody @Validated ItemRequestDto itemRequestDto) {
        return itemRequestClient.addItemRequest(requesterId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.getUserRequests(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUserRequests(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long requesterId,
            @Min(0) @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Min(1) @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return itemRequestClient.getOtherUserRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @NotNull @PathVariable Long requestId) {
        return itemRequestClient.getRequest(requestId, userId);
    }
}