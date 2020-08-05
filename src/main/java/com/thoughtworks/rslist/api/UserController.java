package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping("/user/list")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping("/user")
    public void registerUser(@RequestBody @Valid User user) {
        users.add(user);
    }
}
