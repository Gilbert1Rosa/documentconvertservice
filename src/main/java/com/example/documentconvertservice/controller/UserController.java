package com.example.documentconvertservice.controller;

import com.example.documentconvertservice.dto.LoginResponse;
import com.example.documentconvertservice.dto.UserDTO;
import com.example.documentconvertservice.security.JWTUtil;
import com.example.documentconvertservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${application.security.ttl}")
    private Integer jwtTtl;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("AUth Failed!");
        }
        String token = jwtUtil.generate(username, jwtTtl);

        return ResponseEntity.ok(new LoginResponse(
                token,
                jwtUtil.extractCreatedAt(token),
                jwtUtil.extractExpirationDate(token)
        ));
    }

    @GetMapping("/admin/list")
    public ResponseEntity<?> listUsers(
            Pageable pageable
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "isAdmin", defaultValue = "false") Boolean isAdmin
    ) {
        return ResponseEntity.ok(userService.addUser(username, password, isAdmin));
    }

    @PutMapping("/admin/modify")
    public ResponseEntity<?> modifyUser(
            @RequestParam("user") UserDTO user
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/admin/deactivate")
    public ResponseEntity<?> deactivateUser(
            @RequestParam("username") String username
    ) {
        return ResponseEntity.ok(null);
    }
}
