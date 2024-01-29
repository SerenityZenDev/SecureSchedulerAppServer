package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping("")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto){
        return null;
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable int commentId){
        return null;
    }

    @DeleteMapping("/{commentId}")
    public CommentResponseDto deleteComment(@PathVariable int commentId){
        return null;
    }

}
