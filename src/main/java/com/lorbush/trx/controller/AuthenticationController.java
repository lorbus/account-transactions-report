package com.lorbush.trx.controller;

import com.lorbush.trx.repository.UserRepository;
import com.lorbush.trx.security.jwt.JwtTokenProvider;
import com.lorbush.trx.view.model.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();

            UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                    new UsernamePasswordAuthenticationToken(username, data.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthToken);

            String token = jwtTokenProvider.createToken(username, this.users.findByUsername(username).orElseThrow(()
                    -> new UsernameNotFoundException("Username " + username + "not found")).getRolesNamesAsStrings());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            log.debug("Call AuthenticationController /signin ...");

            return ok(model);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}