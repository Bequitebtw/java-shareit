package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {

    public static BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .build();
    }

    public static Booking bookingNewRequestToBooking(BookingNewRequest bookingNewRequest, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingNewRequest.getStart());
        booking.setEnd(bookingNewRequest.getEnd());
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        return booking;
    }
}
