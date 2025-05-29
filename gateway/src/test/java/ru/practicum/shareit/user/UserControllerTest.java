package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserClient userClient;

    private final NewUserRequest newUserRequest = NewUserRequest.builder()
            .name("Test User")
            .email("test@example.com")
            .build();

    private final UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
            .name("Updated User")
            .email("updated@example.com")
            .build();

    @Test
    void getAllUsers_ShouldReturnOk() throws Exception {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_ShouldReturnOk() throws Exception {
        when(userClient.getUserById(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_ShouldValidateInput() throws Exception {
        NewUserRequest invalidRequest = NewUserRequest.builder()
                .name(" ")
                .email("invalid-email")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        when(userClient.updateUser(any(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldValidateInput() throws Exception {
        UpdateUserRequest invalidRequest = UpdateUserRequest.builder()
                .email("invalid-email")
                .build();

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        when(userClient.deleteUser(anyLong()))
                .thenReturn(ResponseEntity.noContent().build());

        mvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}