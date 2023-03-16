package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Long bookingId = Long.valueOf(1);

    @Override
    public BookingDto addBooking(BookingDtoCreate bookingDtoCreate, Long bookerId) {
        if (bookingDtoCreate.getItemId() == null || bookingDtoCreate.getStart() == null
            || bookingDtoCreate.getEnd() == null ||
                    bookingDtoCreate.getStart().equals(bookingDtoCreate.getEnd())) {
            throw new NotValidException("Item isn't set or not valid");
        }
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoSuchItemException("User with ID " +
                    bookerId + " wasn't found"));
        Item item = itemRepository.findById(bookingDtoCreate.getItemId())
                .orElseThrow(() -> new NoSuchItemException("Item with ID " +
                        bookingDtoCreate.getItemId() + " wasn't found"));
        if (item.getOwner().getId().equals(bookerId)) {
            throw new NoSuchItemException("Item with ID " + item.getId() + " belongs to " +
                    "User with ID" + bookerId);
        }
        if (!item.getAvailable()) {
            throw new NotValidException("Item with ID " + item.getId() + " isn't available");
        }
        if (bookingDtoCreate.getEnd().isBefore(LocalDateTime.now()) ||
            bookingDtoCreate.getEnd().isBefore(bookingDtoCreate.getStart()) ||
                bookingDtoCreate.getStart().isBefore(LocalDateTime.now())) {
            throw new NotValidException("Start or End LocalDateTime isn't valid");
        }
        Booking booking = BookingMapper.toBookingFromDtoCreate(bookingDtoCreate, bookingId, user, item);
        booking.setId(bookingId);
        bookingRepository.save(booking);
        ++bookingId;
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto setApprove(Long bookingId, Long bookerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchItemException("Booking with ID " +
                        bookingId + " wasn't found"));
        if (booking.getBooker().getId().equals(bookerId)) {
            throw new NoSuchItemException("This is your Item");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new NotValidException("Booking already approved");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long bookerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchItemException("Booking with ID " +
                        bookingId + " wasn't found"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NoSuchItemException("Item with ID " +
                        booking.getItem().getId() + " wasn't found"));
        if (!booking.getBooker().getId().equals(bookerId) &&
                !item.getOwner().getId().equals(bookerId)) {
            throw new NoSuchItemException("User with ID " + bookerId + " doesn't have rights" +
                    "to get booking with ID " + bookingId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(String state, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("User with ID " +
                        userId + " wasn't found"));
        Sort sort = Sort.by("start").descending();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "APPROVED":
            case "REJECTED":
                return bookingRepository.findAll()
                        .stream()
                        .sorted((a, b) -> a.getStart().isBefore(b.getStart()) ? 1 : -1)
                        .filter(s -> s.getBooker().getId().equals(userId))
                        .filter(s -> s.getStatus().toString().equals(state))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(), sort)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(), sort)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findCurrentBookerBookings(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new NotValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getUserItemsBookings(String state, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchItemException("User with ID " +
                        userId + " wasn't found"));
        Sort sort = Sort.by("start").descending();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "APPROVED":
            case "REJECTED":
                return bookingRepository.findAll()
                        .stream()
                        .sorted((a, b) -> a.getStart().isBefore(b.getStart()) ? 1 : -1)
                        .filter(s -> s.getItem().getOwner().getId().equals(userId))
                        .filter(s -> s.getStatus().toString().equals(state))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), sort)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), sort)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findBookingsByItemOwnerCurrent(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new NotValidException("Unknown state: UNSUPPORTED_STATUS");
        }

    }
}