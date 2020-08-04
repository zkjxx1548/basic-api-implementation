package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_re_list () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rs/list"))
        .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}