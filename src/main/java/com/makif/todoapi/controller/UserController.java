package com.makif.todoapi.controller;

import com.makif.todoapi.entity.User;
import com.makif.todoapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody User newUser) throws Exception {
       User user = userService.createUser(newUser);
       return ResponseEntity.ok(user);
    }
}
