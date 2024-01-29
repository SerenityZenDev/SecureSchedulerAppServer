package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
}
