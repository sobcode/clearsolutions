package com.app.clearsolutions.controller;

import com.app.clearsolutions.model.dto.DataDTO;
import com.app.clearsolutions.model.dto.UserDTO;
import com.app.clearsolutions.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private final UserDTO userDTO;

    {
        userDTO = new UserDTO(2, "pavlo@gmail.com", "Pavlo", "Biruk",
                LocalDate.of(2000, 5, 10), "Kharkiv", "+380667778899");
    }

    @Test
    public void testCreateUser_thenHttpStatusIsCreated() throws Exception {
        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(userService.createUser(any(UserDTO.class), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.created(URI.create("")).build());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void testReadUsers_thenHttpStatusIsOk() throws Exception {
        when(userService.getUsers(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(ResponseEntity.ok(new DataDTO(userDTO)));

        mockMvc.perform(get("/api/users")
                        .param("from", "2000-01-01")
                        .param("to", "2005-01-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testRadUserById_thenHttpStatusIsOk() throws Exception {
        long id = 1L;

        when(userService.getUserById(anyLong()))
                .thenReturn(ResponseEntity.ok(new DataDTO(userDTO)));

        mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteUser_thenHttpStatusIsOk() throws Exception {
        long id = 1L;

        when(userService.deleteUser(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUser_thenHttpStatusIsOk() throws Exception {
        long id = 1L;
        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(userService.updateAllUserFields(any(UserDTO.class), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testPartialUpdateUser_thenHttpStatusIsOk() throws Exception {
        long id = 1L;
        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(userService.partialUpdateUser(any(UserDTO.class), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
