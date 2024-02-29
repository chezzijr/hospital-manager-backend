package org.hospitalmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.hospitalmanager.models.User;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/user")
    public User user() {
        return new User("John Doe", "123", "456");
    }
}
