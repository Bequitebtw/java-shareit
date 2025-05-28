package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.ItemRequestClient;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.dto.NewItemRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private final NewItemRequest newItemRequest = NewItemRequest.builder()
            .description("Нужна мощная дрель")
            .build();

    @Test
    void getAllRequests_ShouldReturnOk() throws Exception {
        when(itemRequestClient.getAllRequests())
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getUserRequests_ShouldReturnOk() throws Exception {
        when(itemRequestClient.getUserRequests(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_ShouldReturnOk() throws Exception {
        when(itemRequestClient.getRequestById(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void createItemRequest_ShouldReturnCreated() throws Exception {
        when(itemRequestClient.createItemRequest(any(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserRequests_ShouldRequireUserIdHeader() throws Exception {
        mvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemRequest_ShouldRequireUserIdHeader() throws Exception {
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}