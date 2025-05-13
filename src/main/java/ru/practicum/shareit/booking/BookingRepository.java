package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
                SELECT b FROM Booking b
                JOIN FETCH b.item i
                JOIN FETCH b.booker u
                WHERE b.booker.id = :bookerId AND b.status = :status
                ORDER BY b.start DESC
            """)
    List<Booking> findBookingsByBookerAndStatus(
            @Param("bookerId") Long bookerId,
            @Param("status") BookingStatus status
    );

    @Query("""
                SELECT b FROM Booking b
                JOIN FETCH b.item i
                JOIN FETCH b.booker u
                WHERE b.booker.id = :bookerId
                ORDER BY b.start DESC
            """)
    List<Booking> findBookingsByBooker(@Param("bookerId") long bookerId);

    @Query("""
                SELECT b FROM Booking b
                JOIN FETCH b.item i
                JOIN FETCH i.user owner
                JOIN FETCH b.booker u
                WHERE i.user.id = :ownerId
                ORDER BY b.start DESC
            """)
    List<Booking> findBookingsByOwner(@Param("ownerId") long ownerId);

    @Query("""
                SELECT b FROM Booking b
                JOIN FETCH b.item i
                JOIN FETCH i.user owner
                JOIN FETCH b.booker u
                WHERE i.user.id = :ownerId AND b.status = :status
                ORDER BY b.start DESC
            """)
    List<Booking> findBookingsByOwnerAndStatus(
            @Param("ownerId") long ownerId,
            @Param("status") BookingStatus status
    );

    @Query("""
                SELECT MAX(b.end) FROM Booking b
                WHERE b.item.id = :itemId
            """)
    LocalDateTime findLastBookingEndDateByItemId(@Param("itemId") Long itemId);

    boolean existsByBookerIdAndItemIdAndStatus(Long bookerId, Long itemId, BookingStatus status);
}
