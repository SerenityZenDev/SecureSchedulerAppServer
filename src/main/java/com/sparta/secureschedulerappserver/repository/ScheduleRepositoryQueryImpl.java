package com.sparta.secureschedulerappserver.repository;


import static com.sparta.secureschedulerappserver.entity.QSchedule.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.secureschedulerappserver.entity.QSchedule;
import com.sparta.secureschedulerappserver.entity.Schedule;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleRepositoryQueryImpl implements ScheduleRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Schedule> searchByTitle(String title) {
        var result = jpaQueryFactory.selectFrom(schedule)
            .where(schedule.title.contains(title))
            .fetch();

        return result;
    }
}

