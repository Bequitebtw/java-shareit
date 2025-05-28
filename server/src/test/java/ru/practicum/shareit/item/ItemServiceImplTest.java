package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @MockBean
    private ItemRequestService itemRequestService;

    private User owner;
    private User booker;
    private Item item1;
    private Item item2;
    private Booking pastBooking;
    private Comment comment;

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

        item1 = new Item();
        item1.setName("Дрель");
        item1.setDescription("Простая дрель");
        item1.setAvailable(true);
        item1.setUser(owner);
        itemRepository.save(item1);

        item2 = new Item();
        item2.setName("Молоток");
        item2.setDescription("Стандартный молоток");
        item2.setAvailable(true);
        item2.setUser(owner);
        itemRepository.save(item2);

        pastBooking = new Booking();
        pastBooking.setItem(item1);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        pastBooking.setStart(LocalDateTime.now().minusDays(2));
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(pastBooking);

        comment = new Comment();
        comment.setText("Хорошая дрель");
        comment.setItem(item1);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
    }

//    @Test
//    void getUserItems_ShouldReturnOwnerItems() {
//        List<ItemDto> items = itemService.getUserItems(owner.getId());
//
//        assertThat(items, hasSize(2));
//        assertThat(items.get(0).getName(), equalTo("Дрель"));
//        assertThat(items.get(1).getName(), equalTo("Молоток"));
//    }
//
//    @Test
//    void getItemsByQuery_ShouldReturnAvailableItems() {
//        List<ItemDto> items = itemService.getItemsByQuery("дрель");
//
//        assertThat(items, hasSize(1));
//        assertThat(items.get(0).getName(), equalTo("Дрель"));
//    }
//
//    @Test
//    void getItemsByQuery_ShouldReturnEmptyListForEmptyQuery() {
//        List<ItemDto> items = itemService.getItemsByQuery("");
//
//        assertThat(items, empty());
//    }
//
//    @Test
//    void getItemById_ShouldReturnItem() {
//        ItemDto item = itemService.getItemById(item1.getId());
//
//        assertThat(item.getName(), equalTo("Дрель"));
//        assertThat(item.getDescription(), equalTo("Простая дрель"));
//    }
//
//    @Test
//    void getItemById_ShouldThrowWhenNotFound() {
//        assertThrows(NotFoundItemException.class, () ->
//                itemService.getItemById(999L));
//    }
//
//    @Test
//    void createItem_ShouldCreateNewItem() {
//        NewItemRequest newItem = new NewItemRequest();
//        newItem.setName("Новый предмет");
//        newItem.setDescription("Новое описание");
//        newItem.setAvailable(true);
//
//        ItemDto created = itemService.createItem(owner.getId(), newItem);
//
//        assertThat(created.getName(), equalTo("Новый предмет"));
//        assertThat(created.getDescription(), equalTo("Новое описание"));
//    }
//
//    @Test
//    void createItem_ShouldThrowWhenUserNotFound() {
//        NewItemRequest newItem = new NewItemRequest();
//        newItem.setName("Новый предмет");
//
//        assertThrows(NotFoundUserException.class, () ->
//                itemService.createItem(999L, newItem));
//    }
//
//    @Test
//    void updateItem_ShouldUpdateFields() {
//        UpdateItemRequest update = new UpdateItemRequest();
//        update.setName("Обновленная дрель");
//        update.setDescription("Новое описание");
//
//        ItemDto updated = itemService.updateItem(owner.getId(), update, item1.getId());
//
//        assertThat(updated.getName(), equalTo("Обновленная дрель"));
//        assertThat(updated.getDescription(), equalTo("Новое описание"));
//    }
//
//    @Test
//    void createComment_ShouldCreateComment() {
//        Comment newComment = new Comment();
//        newComment.setText("Отличный инструмент");
//
//        Comment created = itemService.createComment(item1.getId(), booker.getId(), newComment);
//
//        assertThat(created.getText(), equalTo("Отличный инструмент"));
//        assertThat(created.getAuthor().getId(), equalTo(booker.getId()));
//    }
//
//    @Test
//    void createComment_ShouldThrowWhenNoBooking() {
//        Comment newComment = new Comment();
//        newComment.setText("Попытка комментирования");
//
//        assertThrows(NoAccessToItemException.class, () ->
//                itemService.createComment(item1.getId(), owner.getId(), newComment));
//    }

    @Test
    void createComment_ShouldThrowWhenBookingNotFinished() {
        Booking currentBooking = new Booking();
        currentBooking.setItem(item1);
        currentBooking.setBooker(booker);
        currentBooking.setStatus(BookingStatus.APPROVED);
        currentBooking.setStart(LocalDateTime.now().minusHours(1));
        currentBooking.setEnd(LocalDateTime.now().plusHours(1));
        bookingRepository.save(currentBooking);

        Comment newComment = new Comment();
        newComment.setText("Попытка комментирования");

        assertThrows(BadRequestException.class, () ->
                itemService.createComment(item1.getId(), booker.getId(), newComment));
    }
}