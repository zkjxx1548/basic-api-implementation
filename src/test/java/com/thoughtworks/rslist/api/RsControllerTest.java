package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void should_return_event_given_index() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_return_event_given_start_and_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end="))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?end=1"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void should_add_event_given_new_event() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonStr = objectMapper.writeValueAsString(new RsEvent("添加一条热搜", "娱乐", user));

        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.index", is("3")))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无")))
                .andExpect(jsonPath("$[3].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[3].keyWord", is("娱乐")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void should_not_add_event_given_invalid_event() throws Exception {
        User user = new User("xiaowang", "female", 17, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonStr = objectMapper.writeValueAsString(new RsEvent("添加一条热搜", "娱乐", user));

        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void should_only_add_event_to_rs_events_given_same_user_name_event() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonStr = objectMapper.writeValueAsString(new RsEvent("添加第二条热搜", "政治", user));

        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());


    }

    @Test
    @Order(6)
    void should_add_event_and_add_user_given_diff_user_name_event() throws Exception {
        User user = new User("xiaohong", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonStr = objectMapper.writeValueAsString(new RsEvent("添加第三条热搜", "政治", user));

        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());

    }

    @Test
    @Order(7)
    void should_not_add_event_given_null_user_or_null_keyword_or_null_event_name() throws Exception {
        User user = new User("xiaohong", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonStr = objectMapper.writeValueAsString(new RsEvent("添加第三条热搜", null, user));
        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        jsonStr = objectMapper.writeValueAsString(new RsEvent(null, "政治", user));
        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        jsonStr = objectMapper.writeValueAsString(new RsEvent("添加第三条热搜", "政治", null));
        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        jsonStr = objectMapper.writeValueAsString(new RsEvent("添加第三条热搜", "政治", new User()));
        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void should_modify_event_given_new_event() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);


        String jsonStr = objectMapper.writeValueAsString(new RsEvent("该条名字修改了", null, user));
        mockMvc.perform(patch("/rs/event/1").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无")))
                .andExpect(jsonPath("$[3].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[3].keyWord", is("娱乐")))
                .andExpect(status().isOk());

        jsonStr = objectMapper.writeValueAsString(new RsEvent(null, "该条关键字改了", user));
        mockMvc.perform(patch("/rs/event/2").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("该条关键字改了")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无")))
                .andExpect(jsonPath("$[3].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[3].keyWord", is("娱乐")))
                .andExpect(status().isOk());

        jsonStr = objectMapper.writeValueAsString(new RsEvent("该条名字修改了", "该条关键字改了", user));
        mockMvc.perform(patch("/rs/event/3").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("该条关键字改了")))
                .andExpect(jsonPath("$[2].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[2].keyWord", is("该条关键字改了")))
                .andExpect(jsonPath("$[3].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[3].keyWord", is("娱乐")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void should_delete_event_given_index() throws Exception {
        mockMvc.perform(delete("/rs/event?delete=4"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[0].keyWord", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("该条关键字改了")))
                .andExpect(jsonPath("$[2].eventName", is("该条名字修改了")))
                .andExpect(jsonPath("$[2].keyWord", is("该条关键字改了")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_index_exception() throws Exception {
        mockMvc.perform(get("/rs/list/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    public void should_throw_param_exception() throws Exception {
        String jsonStr = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxia1111111\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";

        mockMvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }
}