package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok(null);
    }


    @PostMapping("/create")
    public ResponseEntity<?> addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(name = "email", defaultValue = "") String email
    ) {
        return ResponseEntity.ok(userService.addUser(username, password, email));
    }
}
