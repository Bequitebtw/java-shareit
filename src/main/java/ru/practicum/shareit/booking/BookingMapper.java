package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static BookingDto bookingToBookingDto(Booking booking){
        return new BookingDto(
          booking.getId(),
          booking.getStart(),
          booking.getEnd(),
          booking.getItem(),
          booking.getBooker(),
          booking.getStatus()
        );
    }
    public static Booking bookingNewRequestToBooking(BookingNewRequest bookingNewRequest, Item item, User booker){
        Booking booking = new Booking();
        booking.setStart(bookingNewRequest.getStart());
        booking.setEnd(bookingNewRequest.getEnd());
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        return booking;
    }
}
