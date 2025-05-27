package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingClient bookingClient;

    private final NewBookingRequest newBookingRequest = NewBookingRequest.builder()
            .itemId(1)
            .start(LocalDateTime.now().plusHours(10))
            .end(LocalDateTime.now().plusHours(20))
            .build();

    @Test
    void createBookingTest() throws Exception {
        when(bookingClient.createBooking(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(newBookingRequest));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(newBookingRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId", is((int) newBookingRequest.getItemId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", startsWith(newBookingRequest.getStart().toString().substring(0, 19))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end", startsWith(newBookingRequest.getEnd().toString().substring(0, 19))));
    }
}
