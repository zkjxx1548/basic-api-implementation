package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final static List<User> users = new ArrayList<>();



    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        users.add(user);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("user/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable int id) {

    }
}
