package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

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
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(new User("Tom", "male", 18, "1234678@tw.com", "12345678910"));

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
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
    public void should_throw_user_exception() throws Exception {
        String jsonStr = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxia1111111\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }
}