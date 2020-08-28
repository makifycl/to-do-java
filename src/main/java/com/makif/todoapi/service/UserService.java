package com.makif.todoapi.service;

import com.makif.todoapi.entity.User;
import com.makif.todoapi.repository.UserRepository;
import com.makif.todoapi.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;


    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO runtimeexp
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
    }

    @Transactional
    public User createUser(User user) throws RuntimeException {

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            throw new RuntimeException(user.getUsername() + " is already defined");
        }

       try {
           //TODO password encrypt
           user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
           return userRepository.save(user);
       } catch (Exception e) {
            throw new RuntimeException("User cannot save", e);
       }
    }

    public String authenticateUser(User user) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = loadUserByUsername(user.getUsername());
        return jwtUtil.generatetoken(userDetails);

    }
}
