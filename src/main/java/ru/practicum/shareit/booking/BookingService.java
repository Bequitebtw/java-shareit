package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.awt.print.Book;

public interface BookingService {

    BookingDto createBookingRequest(long userId, BookingNewRequest bookingNewRequest);
    BookingDto answerForBookingRequest(long userId,long bookingId);
    BookingDto getBookingById(long userId,long bookingId);
    BookingDto getBookings(long userId,String state);
    BookingDto getOwnerBookings(long userId,String state);


}
