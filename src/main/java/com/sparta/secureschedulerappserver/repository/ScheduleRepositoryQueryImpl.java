package com.sparta.secureschedulerappserver.repository;


import static com.sparta.secureschedulerappserver.entity.QSchedule.schedule;
import static com.sparta.secureschedulerappserver.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ScheduleRepositoryQueryImpl implements ScheduleRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Schedule> searchByTitle(String title, Pageable pageable) {
        JPAQuery<Schedule> query = jpaQueryFactory.selectFrom(schedule)
            .where(schedule.title.contains(title));

        List<Schedule> schedules = query.offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(schedules, pageable, total);
    }

    @Override
    public Page<Schedule> getSchedulesForUser(Long userId, Pageable pageable) {
        // 사용자의 스케줄을 가져오는 쿼리 생성
        JPAQuery<Schedule> query = jpaQueryFactory.selectFrom(schedule)
            .join(schedule.user, user)
            .fetchJoin() // 사용자 필드를 fetch join하여 Lazy 로딩
            .where(user.userId.eq(userId));

        // 페이징 처리
        List<Schedule> schedules = query.offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 항목 수 조회
        long total = query.fetchCount();


        // 페이징 처리된 결과를 Page 객체로 변환하여 반환
        return new PageImpl<>(schedules, pageable, total);
    }
}

