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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;


    @Override
    public CommentResponseDto createComment(
        Long scheduleId,
        CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails
    ) {
        Schedule schedule = findScheduleById(scheduleId);
        Comment comment = new Comment(commentRequestDto.getComment(), schedule,
            userDetails.getUser());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(
        Long commentId,
        CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails
    ) {

        Comment comment = findCommentById(commentId, userDetails);

        comment.update(commentRequestDto.getComment());

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {

        Comment comment = findCommentById(commentId, userDetails);

        commentRepository.delete(comment);

    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(NotFoundScheduleException::new);
    }

    private Comment findCommentById(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            NotFoundCommentException::new
        );

        if ((Objects.equals(comment.getUser().getUserId(), userDetails.getUser().getUserId()))) {
            return comment;
        }
        throw new UnauthorizedOperationException();
    }
}
