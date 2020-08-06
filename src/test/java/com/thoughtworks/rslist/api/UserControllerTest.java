package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void should_return_users_when_get() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("Jim", "male", 18, "1234678@tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("Jim")))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_age", is(18)))
                .andExpect(jsonPath("$[0].user_email", is("1234678@tw.com")))
                .andExpect(jsonPath("$[0].user_phone", is("12345678910")))
                .andExpect(jsonPath("$[0]", not(hasKey("user_voteNum"))))
                .andExpect(jsonPath("$[0]", not(hasKey("voteNum"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user() throws Exception {
        String request = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678@tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserDto> allUser = userRepository.findAll();
        assertEquals(1, allUser.size());
        assertEquals("Tom", allUser.get(0).getUserName());
    }

    @Test
    void user_name_should_less_than_8() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("TomAndBob", "male", 18, "1234678@tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_age_should_between_18_and_100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("Tom", "male", 17, "1234678@tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_email_should_suit_format() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_phone_should_suit_format() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678@tw.com", "1234567890"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_user_by_id() throws Exception {
        String request = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678@tw.com", "12345678910"));
        mockMvc.perform(post("/user").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.userName", is("Tom")))
                .andExpect(status().isOk());
    }

    @Test
    void     should_delete_user_by_id() throws Exception {
        String request = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678@tw.com", "12345678910"));
        mockMvc.perform(post("/user").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/user/delete/1"));
        List<UserDto> allUser = userRepository.findAll();
        assertEquals(0, allUser.size());

    }
}