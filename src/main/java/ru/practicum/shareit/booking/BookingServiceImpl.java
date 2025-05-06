package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundBookingException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.JpaItemRepository;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final JpaItemRepository itemRepository;
    private final JpaUserRepository userRepository;
    @Override
    @Transactional
    public BookingDto createBookingRequest(long userId, BookingNewRequest bookingNewRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundUserException(userId));
        Item item = itemRepository.findById(bookingNewRequest.getItemId())
                .orElseThrow(()->new NotFoundItemException(bookingNewRequest.getItemId()));
        if(!item.getAvailable()){
            throw new NotAvailableItemException();
        }
        item.setAvailable(false);
        itemRepository.save(item);
        Booking booking = bookingRepository.save(BookingMapper.bookingNewRequestToBooking(bookingNewRequest,item,user));
        return BookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public BookingDto answerForBookingRequest(long userId, long bookingId) {
        return null;
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new NotFoundBookingException(bookingId));
        return BookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public BookingDto getBookings(long userId, String state) {
        return null;
    }

    @Override
    public BookingDto getOwnerBookings(long userId, String state) {
        return null;
    }
}
