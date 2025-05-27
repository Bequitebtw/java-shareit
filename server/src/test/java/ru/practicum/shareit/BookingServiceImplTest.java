package ru.practicum.shareit;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig
public class BookingServiceImplTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private User user1;
    private User user2;
    private Item item;

    private Booking booking;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setName("Алексей");
        user1.setEmail("alex@gmail.com");
        userRepository.save(user1);

        user2 = new User();
        user2.setName("Артем");
        user2.setEmail("artem@gmail.com");
        userRepository.save(user2);

        item = new Item();
        item.setName("Книга");
        item.setDescription("Интересная книга");
        item.setAvailable(true);
        item.setLastBooking(LocalDateTime.now().minusDays(1));
        item.setNextBooking(LocalDateTime.now().plusDays(1));
        item.setUser(user1);
        user1.getItems().add(item);
        itemRepository.save(item);


        booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusHours(10));
        booking.setEnd(LocalDateTime.now().plusHours(12));
        booking.setBooker(user2);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
    }

    @Test
    void approveBookingRequest() {
        bookingService.approveBookingRequest(user1.getId(), booking.getId(), true);
        Booking testBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(testBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }


}
