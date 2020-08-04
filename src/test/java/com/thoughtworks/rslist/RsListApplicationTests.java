package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RsListApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_event_given_index() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_event_given_start_and_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().string("第一条事件, 第二条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end="))
                .andExpect(content().string("第一条事件, 第二条事件, 第三条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=&end=1"))
                .andExpect(content().string("第一条事件, 第二条事件, 第三条事件"))
                .andExpect(status().isOk());
    }

    @Test
    void contextLoads() {
    }

}
