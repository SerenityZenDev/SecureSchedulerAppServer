package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findBySchedule_ScheduleId(Long scheduleId);
}
