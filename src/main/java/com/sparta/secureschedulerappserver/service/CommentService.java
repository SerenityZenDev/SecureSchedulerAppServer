package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.entity.Comment;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.CommentRepository;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    public CommentResponseDto createComment(String scheduleId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Schedule schedule = scheduleRepository.findByUser_UserId(user.getUserId()).get(Integer.parseInt(scheduleId)-1);
        Comment comment = new Comment(commentRequestDto, schedule);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(int commentId, String scheduleId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Schedule schedule = scheduleRepository.findByUser_UserId(user.getUserId()).get(Integer.parseInt(scheduleId)-1);
        Comment comment = commentRepository.findBySchedule_ScheduleId(schedule.getScheduleId()).get(commentId-1);

        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }

    public CommentResponseDto deleteComment(int commentId, String scheduleId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Schedule schedule = scheduleRepository.findByUser_UserId(user.getUserId()).get(Integer.parseInt(scheduleId)-1);
        Comment comment = commentRepository.findBySchedule_ScheduleId(schedule.getScheduleId()).get(commentId-1);

        commentRepository.delete(comment);

        return new CommentResponseDto(comment);
    }
}
