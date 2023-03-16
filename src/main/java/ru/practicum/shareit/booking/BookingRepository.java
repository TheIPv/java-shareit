package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long bookerId);
    List<Booking> findAllByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    List<Booking> findAllByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);
    List<Booking> findAllByItemOwnerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    List<Booking> findAllByItemOwnerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);
    @Query("select b from bookings b " +
            "where b.booker.id = ?1 and " +
            "b.start < ?2 and " +
            "b.end > ?2 " +
            "order by b.start desc")
    List<Booking> findCurrentBookerBookings(long bookerId, LocalDateTime now);

    @Query("select b from bookings b " +
            "where b.item.owner.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    List<Booking> findBookingsByItemOwnerCurrent(Long userId, LocalDateTime now);
}
