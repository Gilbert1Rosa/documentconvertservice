package com.example.documentconvertservice.service;

import com.example.documentconvertservice.data.User;
import com.example.documentconvertservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public Boolean addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setAuthorities(User.UserRole.REGULAR.name());

        userRepository.save(user);

        return true;
    }
}
