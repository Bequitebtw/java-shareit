package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingClient bookingClient;

    private final NewBookingRequest newBookingRequest = NewBookingRequest.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusHours(2))
            .build();

    @Test
    void createBooking_ShouldReturnOk() throws Exception {
        when(bookingClient.createBooking(anyLong(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(newBookingRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_ShouldReturnOk() throws Exception {
        when(bookingClient.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_ShouldReturnOk() throws Exception {
        when(bookingClient.getBookingById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_ShouldReturnOk() throws Exception {
        when(bookingClient.getBookings(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings?state=all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }


    @Test
    void getOwnerBookings_ShouldReturnOk() throws Exception {
        when(bookingClient.getOwnerBookings(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings/owner?state=future")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_ShouldValidateInput() throws Exception {
        NewBookingRequest invalidRequest = NewBookingRequest.builder()
                .itemId(null)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().minusHours(1)) // Неверная дата окончания
                .build();

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(invalidRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}