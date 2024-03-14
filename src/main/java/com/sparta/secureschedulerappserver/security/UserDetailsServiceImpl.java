package com.sparta.secureschedulerappserver.security;

import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    public UserDetails getUser(Long userId, String username) {
        User user = new User(userId, username);
        return new UserDetailsImpl(user);
    }
}
