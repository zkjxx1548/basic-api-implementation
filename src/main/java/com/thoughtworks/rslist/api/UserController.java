package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final static List<User> users = new ArrayList<>();

    @Autowired
    UserRepository userRepository;
   /* @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }
*/
    @PostMapping("/user")
    public void registerUser(@RequestBody @Valid User user) {
        UserDto userDto = new UserDto();
        userDto.setPhone(user.getPhone());
        userDto.setAge(user.getAge());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setVoteNum(user.getVoteNum());
        userDto.setUserName(user.getUserName());
        userRepository.save(userDto);
    }

    @GetMapping("/user/{id}")
    public UserDto getUserById(@PathVariable int id) {
       return userRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
    }
}
