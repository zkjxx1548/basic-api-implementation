package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final static List<User> users = new ArrayList<>();

    @GetMapping("/user/list")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        users.add(user);
        return ResponseEntity.created(null).build();
    }
}
