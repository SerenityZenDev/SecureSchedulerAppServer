package com.sparta.secureschedulerappserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.entity.Comment;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.NotFoundCommentException;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.UnauthorizedOperationException;
import com.sparta.secureschedulerappserver.repository.CommentRepository;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private CommentService commentService;

    @Nested
    @DisplayName("댓글 생성 관련 테스트")
    class createCommentTest{
        @Test
        @DisplayName("댓글 생성 테스트 - 성공")
        void createCommentTestSuccess(){
            // given
            User fakeUser = new User("fakeUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(fakeUser);

            Schedule fakeSchedule = new Schedule("faketitle", "fakecontent", fakeUser);
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(fakeSchedule));

            CommentRequestDto commentRequestDto = new CommentRequestDto("fakeComment");


            //when
            CommentResponseDto commentResponseDto = commentService.createComment(anyLong(), commentRequestDto, userDetails);

            //then
            assertNotNull(commentResponseDto);
            assertEquals(commentResponseDto.getComment(), "fakeComment");
            verify(commentRepository, times(1)).save(any(Comment.class));

        }

        @Test
        @DisplayName("댓글 생성 테스트 - 스케쥴 없음 실패")
        void createCommentTestNotFoundSchedule(){
            // given
            User fakeUser = new User("fakeUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(fakeUser);

            given(scheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            CommentRequestDto commentRequestDto = new CommentRequestDto("fakeComment");

            // when
            // then
            assertThrows(NotFoundScheduleException.class,
                () -> commentService.createComment(anyLong(), commentRequestDto, userDetails));
            verify(commentRepository, never()).save(any());

        }


    }

    @Nested
    @DisplayName("댓글 수정 관련 테스트")
    class updateCommentTest{
        @Test
        @DisplayName("댓글 수정 테스트 - 성공")
        void updateCommentSuccess(){
            // given
            CommentRequestDto commentRequestDto = new CommentRequestDto("fakeCommentRequestDto");

            User user = new User("fakeUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            Schedule schedule = new Schedule("title", "content", user);

            Comment comment = new Comment("Comment", schedule, user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when
            CommentResponseDto commentResponseDto = commentService.updateComment(anyLong(), commentRequestDto, userDetails);

            // then
            assertNotNull(commentResponseDto);
            assertEquals(commentResponseDto.getComment(), "fakeCommentRequestDto");
        }

        @Test
        @DisplayName("댓글 수정 테스트 - 실패(댓글 미확인)")
        void updateCommentNotFoundComment(){
            // given
            CommentRequestDto commentRequestDto = new CommentRequestDto("fakeCommentRequestDto");

            User user = new User("fakeUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            //then
            assertThrows(NotFoundCommentException.class,
                () -> commentService.updateComment(anyLong(), commentRequestDto, userDetails));

        }

        @Test
        @DisplayName("댓글 수정 테스트 - 실패(유저 불일치)")
        void updateCommentUnauthorizedOperation(){
            // given
            CommentRequestDto commentRequestDto = new CommentRequestDto("fakeCommentRequestDto");

            User user = new User("User", "password");
            user.setUserId(1L);
            User fakeUser = new User("fakeUser", "password");
            fakeUser.setUserId(2L);
            UserDetailsImpl userDetails = new UserDetailsImpl(fakeUser);

            Schedule schedule = new Schedule("title", "content", user);

            Comment comment = new Comment("Comment", schedule, user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when
            // then
            assertThrows(UnauthorizedOperationException.class,
                () -> commentService.updateComment(anyLong(), commentRequestDto, userDetails));
        }
    }

    @Nested
    @DisplayName("댓글 삭제 관련 테스트")
    class deleteCommentTest{
        @Test
        @DisplayName("댓글 삭제 테스트 - 성공")
        void deleteCommentTestSuccess(){
            // given
            User user = new User("user", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("title", "content", user);
            Comment comment = new Comment("comment", schedule, user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when
            commentService.deleteComment(anyLong(), userDetails);

            // then
            verify(commentRepository, times(1)).delete(any(Comment.class));

        }

        @Test
        @DisplayName("댓글 삭제 테스트 - 실패(댓글 미확인)")
        void deleteCommentTestNotFoundCommentException(){
            // given
            User user = new User("fakeUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            //then
            assertThrows(NotFoundCommentException.class,
                () -> commentService.deleteComment(anyLong(), userDetails));
        }

        @Test
        @DisplayName("댓글 삭제 테스트 - 실패(유저 불일치)")
        void deleteCommentTestUnauthorizedOperationException(){
            User user = new User("User", "password");
            user.setUserId(1L);
            User fakeUser = new User("fakeUser", "password");
            fakeUser.setUserId(2L);
            UserDetailsImpl userDetails = new UserDetailsImpl(fakeUser);

            Schedule schedule = new Schedule("title", "content", user);

            Comment comment = new Comment("Comment", schedule, user);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when
            // then
            assertThrows(UnauthorizedOperationException.class,
                () -> commentService.deleteComment(anyLong(), userDetails));
        }
    }

}