package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    private User user;

    void setUser(User user) {
        this.user = user;
    }

    @PostMapping("/user")
    public void registerUser(@RequestBody @Valid User user) {
        setUser(user);
    }
}
