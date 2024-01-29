package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
}
