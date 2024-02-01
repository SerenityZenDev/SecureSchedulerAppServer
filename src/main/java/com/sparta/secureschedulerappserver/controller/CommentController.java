package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final CommentService commentService;

    @PostMapping("")
    public CommentResponseDto createComment(
        @PathVariable Long scheduleId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentResponseDto commentResponseDto = commentService.createComment(scheduleId,commentRequestDto, userDetails);
        return commentResponseDto;
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId,commentRequestDto, userDetails);
        return commentResponseDto;
    }

    @DeleteMapping("/{commentId}")
    public CommentResponseDto deleteComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentResponseDto commentResponseDto = commentService.deleteComment(commentId,commentRequestDto, userDetails);
        return commentResponseDto;
    }

}
