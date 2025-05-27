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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.NewItemRequest;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemClient itemClient;

    private final NewItemRequest newItemRequest = NewItemRequest.builder()
            .name("Машина")
            .available(true)
            .description("Вот такая вот крутая")
            .build();

    @Test
    void getItemById() throws Exception {
        long itemId = 1L;
        when(itemClient.getItemById(itemId))
                .thenReturn(ResponseEntity.ok(newItemRequest));

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(newItemRequest.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(newItemRequest.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", is(newItemRequest.getAvailable())));
    }
}
