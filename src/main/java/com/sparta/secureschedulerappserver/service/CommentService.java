package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;

public interface CommentService {

    /**
     * 댓글 생성
     *
     * @param scheduleId        댓글을 생성할 게시글 ID
     * @param commentRequestDto 댓글 생성 내용
     * @param userDetails       댓글 생성자 구분
     * @return 게시글 생성 결과
     */
    CommentResponseDto createComment(Long scheduleId, CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails);

    /**
     * 댓글 수성
     *
     * @param commentId         수정할 댓글 ID
     * @param commentRequestDto 수정할 댓글 내용
     * @param userDetails       댓글 수정자 구분
     * @return 게시글 수정 결과
     */
    CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto,
        UserDetailsImpl userDetails);

    /**
     * 댓글 삭제
     *
     * @param commentId   삭제할 댓글 ID
     * @param userDetails 댓글 삭제자 구분
     */
    void deleteComment(Long commentId, UserDetailsImpl userDetails);
}
