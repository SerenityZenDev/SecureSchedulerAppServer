package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import java.util.List;

public interface ScheduleService {

    /**
     * 게시글 생성
     *
     * @param scheduleRequestDto 게시글 생성 요청정보
     * @param userDetails        게시글 생성 요청자
     * @return 게시글 생성 결과
     */
    ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto,
        UserDetailsImpl userDetails);

    /**
     * 게시글 단건 조회
     *
     * @param scheduleId 게시글 조회 ID
     * @return 게시글 단건 정보
     */
    ScheduleResponseDto readSchedule(Long scheduleId);

    /**
     * 게시글 전체 조회
     *
     * @return 게시글 전체 정보
     */
    ScheduleListResponseDto readSchedules();

    /**
     * 본인 게시글 조회
     *
     * @param userDetails 게시글 조회 요청자
     * @return 게시글 조회 결과
     */
    ScheduleListResponseDto readMySchedules(UserDetailsImpl userDetails);

    /**
     * 미완료 게시글 전체 조회
     *
     * @return 미완료 게시글 전체 조회 결과
     */
    ScheduleListResponseDto readUncompleteSchedules();

    /**
     * 특정 문자 포함 게시글 조회
     *
     * @param text 게시글 포함 키워드
     * @return 게시글 찾기 결과
     */
    List<ScheduleResponseDto> findSchedules(String text);

    /**
     * 게시글 수정
     *
     * @param scheduleId         수정할 게시글 ID
     * @param scheduleRequestDto 수정할 게시글 내용
     * @param userDetails        게시글 수정 요청자
     * @return 게시글 수정 결과
     */
    ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto,
        UserDetailsImpl userDetails);

    /**
     * 게시글 완료
     *
     * @param scheduleId  완료 처리할 게시글 ID
     * @param userDetails 게시글 완료 요청자
     * @return 게시글 완료처리 결과
     */
    ScheduleResponseDto completeSchedule(Long scheduleId, UserDetailsImpl userDetails);

    /**
     * 게시글 숨기기
     *
     * @param scheduleId  숨길 게시글 ID
     * @param userDetails 게시글 숨기기 요청자
     * @return 게시글 숨기기 결과
     */
    ScheduleResponseDto hideSchedule(Long scheduleId, UserDetailsImpl userDetails);

    /**
     * QueryDSL을 통한 제목 키워드 게시글 조회
     *
     * @param keyword   제목에서 검색할 내용
     * @return 게시물 조회 결과
     */
    List<ScheduleResponseDto> showSchedules(String keyword);

    /**
     * QueryDSL + LAZY를 통한 자신이 작성한 게시글 조회
     *
     * @param userDetails 본인 검증 정보
     */
    List<ScheduleResponseDto> getSchedulesForUser(UserDetailsImpl userDetails);
}
