package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.entity.Comment;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.exception.NotFoundCommentException;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.UnauthorizedOperationException;
import com.sparta.secureschedulerappserver.repository.CommentRepository;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;


    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            NotFoundScheduleException::new
        );
        Comment comment = new Comment(commentRequestDto.getComment(), schedule, userDetails.getUser());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            NotFoundCommentException::new
        );

        if (!(comment.getUser().getUserId() == userDetails.getUser().getUserId())) {
            throw new UnauthorizedOperationException();
        }

        comment.update(commentRequestDto.getComment());

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
            NotFoundCommentException::new
        );

        if (!(comment.getUser().getUserId() == userDetails.getUser().getUserId())) {
            throw new UnauthorizedOperationException();
        }

        commentRepository.delete(comment);

    }
}
