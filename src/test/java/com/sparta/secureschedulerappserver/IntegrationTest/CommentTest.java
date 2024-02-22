package com.sparta.secureschedulerappserver.IntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.entity.Comment;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.CommentRepository;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.CommentService;
import com.sparta.secureschedulerappserver.service.ScheduleService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    User user;

    Schedule schedule;

    CommentResponseDto commentResponseDto = null;

    @Test
    @Order(1)
    @DisplayName("댓글 생성")
    void test1(){
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("testcomment");

        user = userRepository.findById(1L).orElse(null);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        schedule = scheduleRepository.findById(1L).orElse(null);

        // when
        commentResponseDto = commentService.createComment(1L, commentRequestDto, userDetails);

        // then
        assertNotNull(commentResponseDto);
        assertEquals(commentResponseDto.getComment(), commentRequestDto.getComment());
    }

    @Test
    @Order(2)
    @DisplayName("댓글 수정")
    void test2(){
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("수정된 댓글");

        user = userRepository.findById(1L).orElse(null);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        schedule = scheduleRepository.findById(1L).orElse(null);

        // when
        commentResponseDto = commentService.updateComment(commentResponseDto.getId(), commentRequestDto, userDetails);

        // then
        assertNotNull(commentResponseDto);
        assertEquals(commentResponseDto.getComment(), commentRequestDto.getComment());
    }

    @Test
    @Order(3)
    @DisplayName("댓글 삭제")
    void test3(){
        // given

        user = userRepository.findById(1L).orElse(null);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        schedule = scheduleRepository.findById(1L).orElse(null);

        // when
        commentService.deleteComment(commentResponseDto.getId(),  userDetails);

        // then
        Optional<Comment> comment = commentRepository.findById(commentResponseDto.getId());
        assertTrue(comment.isEmpty());

    }


}
