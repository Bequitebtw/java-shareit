package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findBookingsByBookerAndStatus(
            @Param("bookerId") Long bookerId,
            @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId ORDER BY b.start DESC")
    List<Booking> findBookingsByBooker(@Param("bookerId") long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.user.id = :ownerId ORDER BY b.start DESC")
    List<Booking> findBookingsByOwner(@Param("ownerId") long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.user.id = :ownerId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findBookingsByOwnerAndStatus(
            @Param("ownerId") long ownerId,
            @Param("status") BookingStatus status
    );

    boolean existsByBookerAndItemAndStatus(User booker, Item item, BookingStatus status);
}
