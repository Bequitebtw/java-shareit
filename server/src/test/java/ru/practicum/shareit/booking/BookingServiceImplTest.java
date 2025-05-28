package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    private User owner;
    private User booker;
    private Item availableItem;
    private Item unavailableItem;
    private Booking pendingBooking;
    private Booking approvedBooking;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@email.com");
        userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@email.com");
        userRepository.save(booker);

        availableItem = new Item();
        availableItem.setName("Available Item");
        availableItem.setDescription("Available Description");
        availableItem.setAvailable(true);
        availableItem.setUser(owner);
        itemRepository.save(availableItem);

        unavailableItem = new Item();
        unavailableItem.setName("Unavailable Item");
        unavailableItem.setDescription("Unavailable Description");
        unavailableItem.setAvailable(false);
        unavailableItem.setUser(owner);
        itemRepository.save(unavailableItem);

        pendingBooking = new Booking();
        pendingBooking.setItem(availableItem);
        pendingBooking.setBooker(booker);
        pendingBooking.setStatus(BookingStatus.WAITING);
        pendingBooking.setStart(LocalDateTime.now().plusHours(1));
        pendingBooking.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.save(pendingBooking);

        approvedBooking = new Booking();
        approvedBooking.setItem(availableItem);
        approvedBooking.setBooker(booker);
        approvedBooking.setStatus(BookingStatus.APPROVED);
        approvedBooking.setStart(LocalDateTime.now().plusHours(3));
        approvedBooking.setEnd(LocalDateTime.now().plusHours(4));
        bookingRepository.save(approvedBooking);
    }

    @Test
    void createBookingRequest_ShouldCreateBooking() {
        BookingNewRequest request = new BookingNewRequest();
        request.setItemId(availableItem.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto result = bookingService.createBookingRequest(booker.getId(), request);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(result.getItem().getId(), equalTo(availableItem.getId()));
        assertThat(result.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    void createBookingRequest_ShouldThrowWhenItemUnavailable() {
        BookingNewRequest request = new BookingNewRequest();
        request.setItemId(unavailableItem.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotAvailableItemException.class, () ->
                bookingService.createBookingRequest(booker.getId(), request));
    }

    @Test
    void createBookingRequest_ShouldThrowWhenUserNotFound() {
        BookingNewRequest request = new BookingNewRequest();
        request.setItemId(availableItem.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotFoundUserException.class, () ->
                bookingService.createBookingRequest(999L, request));
    }


    @Test
    void approveBookingRequest_ShouldThrowWhenNotOwner() {
        assertThrows(NoAccessToItemException.class, () ->
                bookingService.approveBookingRequest(booker.getId(), pendingBooking.getId(), true));
    }

    @Test
    void getBookingById_ShouldReturnForOwner() {
        BookingDto result = bookingService.getBookingById(owner.getId(), pendingBooking.getId());

        assertThat(result.getId(), equalTo(pendingBooking.getId()));
    }

    @Test
    void getBookingById_ShouldReturnForBooker() {
        BookingDto result = bookingService.getBookingById(booker.getId(), pendingBooking.getId());

        assertThat(result.getId(), equalTo(pendingBooking.getId()));
    }

    @Test
    void getBookingById_ShouldThrowWhenNoAccess() {
        User stranger = new User();
        stranger.setName("Stranger");
        stranger.setEmail("stranger@email.com");
        userRepository.save(stranger);

        assertThrows(NoAccessToBookingException.class, () ->
                bookingService.getBookingById(stranger.getId(), pendingBooking.getId()));
    }

    @Test
    void getBookings_ShouldReturnByStatus() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "APPROVED");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), equalTo(approvedBooking.getId()));
    }

    @Test
    void getOwnerBookings_ShouldReturnAllForOwner() {
        List<BookingDto> result = bookingService.getOwnerBookings(owner.getId(), "ALL");

        assertThat(result, hasSize(2));
    }

    @Test
    void getOwnerBookings_ShouldReturnByStatus() {
        List<BookingDto> result = bookingService.getOwnerBookings(owner.getId(), "WAITING");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), equalTo(pendingBooking.getId()));
    }

    @Test
    void parseBookingStatus_ShouldReturnStatus() {
        BookingServiceImpl service = (BookingServiceImpl) bookingService;

        assertThat(service.parseBookingStatus("APPROVED"), equalTo(BookingStatus.APPROVED));
        assertThat(service.parseBookingStatus("WAITING"), equalTo(BookingStatus.WAITING));
        assertThat(service.parseBookingStatus("REJECTED"), equalTo(BookingStatus.REJECTED));
    }

    @Test
    void parseBookingStatus_ShouldThrowWhenInvalid() {
        BookingServiceImpl service = (BookingServiceImpl) bookingService;

        assertThrows(BadRequestException.class, () ->
                service.parseBookingStatus("INVALID_STATUS"));
    }
}