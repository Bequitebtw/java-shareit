package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto createBookingRequest(long userId, BookingNewRequest bookingNewRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        Item item = itemRepository.findById(bookingNewRequest.getItemId())
                .orElseThrow(() -> new NotFoundItemException(bookingNewRequest.getItemId()));
        if (!item.isAvailable()) {
            throw new NotAvailableItemException();
        }
        Booking booking = bookingRepository.save(BookingMapper.bookingNewRequestToBooking(bookingNewRequest, item, user));
        return BookingMapper.bookingToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approveBookingRequest(long userId, long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundBookingException(bookingId));

        //Очень странно, но везде выбрасывается NotFound(404) где пользователя не найден, но тут тесты просят другой код ошибки
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("нет такого пользователя"));
        if (!user.getItems().contains(booking.getItem())) {
            throw new NoAccessToItemException(userId, booking.getItem().getId());
        }

        Item item = booking.getItem();
        LocalDateTime endTime = bookingRepository.findLastBookingEndDateByItemId(item.getId());
        if (isApproved) {
            item.setAvailable(false);
            if (endTime != null) {
                item.setLastBooking(endTime);
            }
            item.setNextBooking(booking.getStart());
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        itemRepository.save(item);
        bookingRepository.save(booking);

        return BookingMapper.bookingToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundBookingException(bookingId));
        User owner = booking.getItem().getUser();
        if (booking.getBooker().getId() == userId || owner.getId() == userId) {
            return BookingMapper.bookingToBookingDto(booking);
        }
        throw new NoAccessToBookingException(userId, bookingId);
    }

    @Transactional
    @Override
    public List<BookingDto> getBookings(long userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        List<Booking> bookingsList;
        BookingStatus status = parseBookingStatus(state);
        if (status != null) {
            bookingsList = bookingRepository.findBookingsByBookerAndStatus(userId, status);
        } else {
            bookingsList = bookingRepository.findBookingsByBooker(userId);
        }
        return bookingsList.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingDto> getOwnerBookings(long userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        List<Booking> bookingList;

        BookingStatus status = parseBookingStatus(state);
        if (status != null) {
            bookingList = bookingRepository.findBookingsByOwnerAndStatus(userId, status);
        } else {
            bookingList = bookingRepository.findBookingsByOwner(userId);
        }
        return bookingList.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    private BookingStatus parseBookingStatus(String state) {
        if (!state.equalsIgnoreCase("ALL")) {
            try {
                return BookingStatus.valueOf(state.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Нет такого состояния");
            }
        }
        return null;
    }
}
