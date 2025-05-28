package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemClient itemClient;

    private final NewItemRequest newItemRequest = NewItemRequest.builder()
            .name("Дрель")
            .description("Простая дрель")
            .available(true)
            .build();

    private final UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
            .name("Обновленная дрель")
            .description("Новое описание")
            .available(false)
            .build();

    private final NewCommentRequest newCommentRequest = NewCommentRequest.builder()
            .text("Отличный инструмент")
            .build();

    @Test
    void getUserItems_ShouldReturnOk() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getItemsByQuery_ShouldReturnOk() throws Exception {
        when(itemClient.getItemsByQuery(anyString()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/items/search?text=дрель"))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_ShouldReturnOk() throws Exception {
        when(itemClient.getItemById(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_ShouldReturnOk() throws Exception {
        when(itemClient.createItem(anyLong(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_ShouldReturnOk() throws Exception {
        when(itemClient.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(updateItemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_ShouldValidateInput() throws Exception {
        NewItemRequest invalidRequest = NewItemRequest.builder()
                .name(" ")
                .description(null)
                .available(null)
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(invalidRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createComment_ShouldValidateInput() throws Exception {
        NewCommentRequest invalidRequest = NewCommentRequest.builder()
                .text(" ")
                .build();

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(invalidRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}