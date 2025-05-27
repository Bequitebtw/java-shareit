package ru.practicum.shareit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class NewBookingRequestJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeWithCorrectDateFormat() throws JsonProcessingException {
        BookingNewRequest request = BookingNewRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2030, 1, 1, 12, 0))
                .end(LocalDateTime.of(2030, 1, 2, 12, 0))
                .build();

        String json = objectMapper.writeValueAsString(request);

        assertThat(json).contains("\"start\":\"2030-01-01T12:00:00\"");
        assertThat(json).contains("\"end\":\"2030-01-02T12:00:00\"");
    }

    @Test
    void shouldDeserializeFromJsonWithCorrectDateFormat() throws JsonProcessingException {
        String json = """
                {
                  "itemId": 1,
                  "start": "2030-01-01T12:00:00",
                  "end": "2030-01-02T12:00:00"
                }
                """;

        BookingNewRequest request = objectMapper.readValue(json, BookingNewRequest.class);

        assertThat(request.getItemId()).isEqualTo(1L);
        assertThat(request.getStart()).isEqualTo(LocalDateTime.of(2030, 1, 1, 12, 0));
        assertThat(request.getEnd()).isEqualTo(LocalDateTime.of(2030, 1, 2, 12, 0));
    }
}
