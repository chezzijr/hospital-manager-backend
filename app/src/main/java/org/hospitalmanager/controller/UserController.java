package org.hospitalmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.hospitalmanager.model.User;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return new User(id, "John Doe", User.Role.ADMIN, "Hello");
    }
}
