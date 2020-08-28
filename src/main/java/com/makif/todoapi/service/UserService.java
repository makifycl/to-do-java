package com.makif.todoapi.service;

import com.makif.todoapi.entity.User;
import com.makif.todoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO runtimeexp
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User is not found"));
    }

    @Transactional
    public User createUser(User user) throws Exception {

        userRepository.findByUsername(user.getUsername()).ifPresent(usr -> {
            throw new RuntimeException(usr.getUsername() + " is already defined");
        });

       try {
           //TODO password encrypt
           user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
           return userRepository.save(user);
       } catch (Exception e) {
            throw new Exception("User cannot save", e);
       }
    }
}
