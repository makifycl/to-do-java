package com.makif.todoapi.controller;

import com.makif.todoapi.entity.User;
import com.makif.todoapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody User newUser) throws RuntimeException {
        //TODO exception handling
       User user = userService.createUser(newUser);
       return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> singIn(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(userService.authenticateUser(user), HttpStatus.OK);
    }
}
