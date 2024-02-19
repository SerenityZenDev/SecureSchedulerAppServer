package com.sparta.secureschedulerappserver.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    @DisplayName("Comment 엔티티 생성 테스트")
    void createCommentEntity() {
        // given
        User user = new User("tester", "password");
        Schedule schedule = new Schedule("테스트 일정", "테스트 내용", user);
        String testComment = "테스트 댓글";

        // when
        Comment comment = new Comment(testComment, schedule, user);

        // then
        assertNotNull(comment);
        assertEquals("테스트 댓글", comment.getComment());
        assertEquals(schedule, comment.getSchedule());
        assertEquals(user, comment.getUser());
    }

    @Test
    @DisplayName("Comment 엔티티 수정 테스트")
    void updateCommentEntity() {
        // given
        User user = new User("tester", "password");
        Schedule schedule = new Schedule("테스트 일정", "테스트 내용", user);
        String beforeCommentText = "테스트 댓글 수정 전";
        String afterCommentText = "테스트 댓글 수정 후";
        Comment comment = new Comment(beforeCommentText, schedule, user);

        // when
        comment.update(afterCommentText);

        // then
        assertEquals("테스트 댓글 수정 후", comment.getComment());
    }


}