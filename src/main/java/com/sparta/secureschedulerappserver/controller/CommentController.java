package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.CommentDeleteDto;
import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final CommentServiceImpl commentService;

    @PostMapping("")
    public CommentResponseDto createComment(
        @PathVariable Long scheduleId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.createComment(scheduleId,
            commentRequestDto, userDetails);
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.updateComment(commentId,
            commentRequestDto, userDetails);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDeleteDto> deleteComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.deleteComment(commentId, userDetails);
        String successMessage = "삭제가 정상적으로 처리되었습니다.";
        return ResponseEntity.ok().body(new CommentDeleteDto(successMessage, HttpStatus.OK));
    }

}
