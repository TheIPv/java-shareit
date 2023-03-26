package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private Long itemRequestId = Long.valueOf(1);
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        if(itemRequestDto.getDescription() == null) {
            throw new NotValidException("Description is empty");
        }
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("Incorrect user ID"));
        itemRequestDto.setRequestor(UserMapper.toUserDto(requestor));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setId(itemRequestId);
        ++itemRequestId;
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("Incorrect user ID"));
        return itemRequestRepository.findItemRequestByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getOtherUserRequests(Long userId, int from, int size) {
        if(size <= 0 || from < 0) {
            throw new NotValidException("Incorrect size or from parametrs");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("Incorrect user ID"));
        return itemRequestRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .filter(s -> !s.getRequestor().getId().equals(userId))
                .limit(size)
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("Incorrect user ID"));
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchItemException("Incorrect item ID")));
    }
}
