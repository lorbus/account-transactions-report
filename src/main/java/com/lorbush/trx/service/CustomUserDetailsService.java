package com.lorbush.trx.service;

import com.lorbush.trx.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.lorbush.trx.entities.User> user = Optional.ofNullable(this.userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found")));
        //log.debug("loadUserByUsername...");
        return new User(user.get().getUsername(), user.get().getPassword(), new HashSet<>());
    }
}
